package com.assignment.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assignment.dto.AssignmentDTO;
import com.assignment.dto.MessageDTO;
import com.assignment.dto.ProjectDTO;
import com.assignment.entity.AssignmentEntity;
import com.assignment.entity.ProjectEntity;
import com.assignment.repository.AssignmentRepository;
import com.assignment.repository.ProjectRepository;

@Service
public class ProjectService {
	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private AssignmentRepository assignmentRepository;

	public void create(ProjectDTO dto) {
		try {
			ProjectEntity entity = new ProjectEntity(dto);
			entity.setStatus(1);
			projectRepository.save(entity);
		} catch (Exception e) {
			throw e;
		}
	}

	public MessageDTO alter(ProjectDTO dto) {
		MessageDTO message = new MessageDTO();
		try {
			ProjectEntity entity = new ProjectEntity(dto);
			projectRepository.save(entity);
			message.setMessage("Project Information Changed Successfully");
		} catch (Exception e) {
			throw e;
		}
		return message;
	}

	public List<ProjectEntity> listAllProjects() {
		List<ProjectEntity> ls = projectRepository.findAll();
		return ls;
	}

	public ProjectDTO listAProject(Integer id) throws Exception {
		ProjectDTO dto = new ProjectDTO();
		Optional<ProjectEntity> obj = projectRepository.findById(id);
		try {
			dto = new ProjectDTO(obj.get());
		} catch (NoSuchElementException e) {
			return null;
		} catch (Exception e) {
			throw e;
		}
		return dto;
	}

	public void assignment(AssignmentDTO dto) {
		try {
			AssignmentEntity entity = new AssignmentEntity(dto);
			assignmentRepository.save(entity);
		} catch (Exception e) {
			throw e;
		}
	}

	public void withdrawAssignment(Integer AssignmentId) {
		try {
			assignmentRepository.deleteById(AssignmentId);
		} catch (Exception e) {
			throw e;
		}
	}
}
