package com.assignment.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Api(description = "Endpoints for Creating, Update Project information, Assign/withdraw employee to project", tags = {
		"Project" })
public class ProjectController {
	@Autowired
	private ProjectService projectService;

	@ApiOperation(value = "API create Project from Admin", notes = "Only Admin can create project")
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

	@ApiOperation(value = "API list all Project from DB", notes = "Only Admin can use this API")
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

	@ApiOperation(value = "API view a Project information", notes = "Only PM and Admin can use this API")
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

	@ApiOperation(value = "API alter Project information", notes = "Only Admin alter project")
	@PutMapping("/projects/{id}")
	public ResponseEntity<?> alterProject(@Valid @RequestBody ProjectDTO dto, @PathVariable Integer id) {
		MessageDTO message = new MessageDTO();
		try {
			dto.setId(id);
			message = projectService.alter(dto);
		} catch (Exception e) {
			message.setMessage("Unknown Error: " + e.getMessage());
		}
		return ResponseEntity.ok(message);
	}

	@ApiOperation(value = "API assign employee to Project", notes = "Only Admin can use this API")
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

	@ApiOperation(value = "API withdraw employee from Project", notes = "Only Admin and PM can use this API")
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
