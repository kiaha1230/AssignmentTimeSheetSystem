/**
 * 
 */
package com.assignment.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.dto.DecideOTRequestDTO;
import com.assignment.dto.MessageDTO;
import com.assignment.dto.OvertimeDTO;
import com.assignment.service.OvertimeService;

/**
 * @author baovd
 *
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OvertimeController {
	@Autowired
	private OvertimeService overtimeService;

	@PostMapping("/overtime")
	public ResponseEntity<?> createOTRequest(@Valid @RequestBody OvertimeDTO dto) {
		MessageDTO message = new MessageDTO();
		try {
			overtimeService.createOTRequest(dto);
		} catch (Exception e) {
			message.setMessage("Unknown Error: " + e.getMessage());
		}
		message.setMessage("Create OT Request Successfully");
		return ResponseEntity.ok(message);
	}

	@PutMapping("/overtime")
	public ResponseEntity<?> decideOTRequest(@Valid @RequestBody DecideOTRequestDTO dto) {
		MessageDTO message = new MessageDTO();
		try {
			overtimeService.confirmOrRejectOT(dto);
			if (dto.getStatus() == -1) {
				message.setMessage("Rejected OT Request");
			} else if (dto.getStatus() == 1) {
				message.setMessage("Approved OT Request");
			}
		} catch (Exception e) {
			message.setMessage("Unknown Error: " + e.getMessage());
		}
		return ResponseEntity.ok(message);
	}
}
