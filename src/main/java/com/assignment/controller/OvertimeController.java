/**
 * 
 */
package com.assignment.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.dto.DecideOTRequestDTO;
import com.assignment.dto.MessageDTO;
import com.assignment.dto.OvertimeDTO;
import com.assignment.service.OvertimeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author baovd
 *
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Api(description = "Endpoints for Creating, Update OT request status", tags = { "Overtime" })
public class OvertimeController {
	@Autowired
	private OvertimeService overtimeService;

	@ApiOperation(value = "API create OT request from employee", notes = "Create OT request and wait for response from PM")
	@PostMapping("/overtime")
	public ResponseEntity<?> createOTRequest(@Valid @RequestBody OvertimeDTO overtimeDTO) {
		MessageDTO message = new MessageDTO();
		try {
			overtimeService.createOTRequest(overtimeDTO);
		} catch (Exception e) {
			message.setMessage("Unknown Error: " + e.getMessage());
		}
		message.setMessage("Create OT Request Successfully");
		return ResponseEntity.ok(message);
	}

	@ApiOperation(value = "API confirm or reject OT request from PM")
	@PutMapping("/overtime/{id}")
	public ResponseEntity<?> decideOTRequest(@Valid @RequestBody DecideOTRequestDTO decideOTRequestDTO,
			@PathVariable Integer id) {
		MessageDTO message = new MessageDTO();
		try {
			decideOTRequestDTO.setId(id);
			overtimeService.confirmOrRejectOT(decideOTRequestDTO);
			if (decideOTRequestDTO.getStatus() == -1) {
				message.setMessage("Rejected OT Request");
			} else if (decideOTRequestDTO.getStatus() == 1) {
				message.setMessage("Approved OT Request");
			}
		} catch (Exception e) {
			message.setMessage("Unknown Error: " + e.getMessage());
		}
		return ResponseEntity.ok(message);
	}
}
