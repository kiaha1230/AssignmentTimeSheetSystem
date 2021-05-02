package com.assignment.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.dto.AssignmentDTO;
import com.assignment.dto.MessageDTO;
import com.assignment.dto.ProjectDTO;
import com.assignment.entity.ProjectEntity;
import com.assignment.service.ProjectService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProjectController {
	@Autowired
	private ProjectService projectService;
	@Autowired
	private MessageSource messageSource;

	@PostMapping("/projects")
	public ResponseEntity<?> createProject(@Valid @RequestBody ProjectDTO dto) {
		MessageDTO message = new MessageDTO();
		try {
			projectService.create(dto);
			message.setMessage("New Project Created Successfully");
		} catch (Exception e) {
			message.setMessage("Unknown Error: " + e.getMessage());
		}
		return ResponseEntity.ok(message);
	}

	@GetMapping("/projects")
	public ResponseEntity<?> viewAllProjects() {
		List<ProjectEntity> ls = projectService.listAllProjects();
		if (ls == null || ls.isEmpty() == true) {
			MessageDTO message = new MessageDTO();
			message.setMessage("Projects list is empty");
			return ResponseEntity.ok(message);
		}

		return ResponseEntity.ok(ls);
	}

	@GetMapping("/projects/{id}")
	public ResponseEntity<?> viewAProjects(@PathVariable Integer id) {
		ProjectDTO obj = new ProjectDTO();
		MessageDTO message = new MessageDTO();
		try {
			obj = projectService.listAProject(id);
		} catch (Exception e) {
			message.setMessage("Unknown Error : " + e.getMessage());
			return ResponseEntity.ok(message);
		}
		if (obj == null) {
			message.setMessage("Project Do Not Exists");
			return ResponseEntity.ok(message);
		}
		return ResponseEntity.ok(obj);
	}

	@PutMapping("/projects")
	public ResponseEntity<?> alterProject(@Valid @RequestBody ProjectDTO dto) {
		MessageDTO message = new MessageDTO();
		try {
			projectService.alter(dto);
		} catch (Exception e) {
			message.setMessage("Unknown Error: " + e.getMessage());
		}
		message.setMessage("Project Information Changed Successfully");
		return ResponseEntity.ok(message);
	}

	@PostMapping("/projects/assignment")
	public ResponseEntity<?> assignment(@Valid @RequestBody AssignmentDTO dto) {
		MessageDTO message = new MessageDTO();
		try {
			projectService.assignment(dto);
		} catch (Exception e) {
			message.setMessage("Unknown Error: " + e.getMessage());
		}
		message.setMessage("Assign Employee To Project Successfully");
		return ResponseEntity.ok(message);
	}

	@DeleteMapping("/projects/assignment/{AssignmentId}")
	public ResponseEntity<?> withdrawAssignmentById(@PathVariable Integer AssignmentId) {
		MessageDTO message = new MessageDTO();
		try {
			projectService.withdrawAssignment(AssignmentId);
		} catch (Exception e) {
			message.setMessage("Unknown Error: " + e.getMessage());
		}
		message.setMessage("Remove Employee From Project Successfully");
		return ResponseEntity.ok(message);
	}
}
