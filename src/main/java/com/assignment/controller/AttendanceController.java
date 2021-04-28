/**
 * 
 */
package com.assignment.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.dto.AttendanceDTO;
import com.assignment.dto.MessageDTO;
import com.assignment.service.AttendanceService;

import antlr.collections.List;

/**
 * @author baovd
 *
 */
@RestController
@RequestMapping("/api")
public class AttendanceController {
	@Autowired
	private AttendanceService attendanceService;

	@PostMapping("/attendance/{employeeId}")
	public ResponseEntity<?> checkIn(@Valid @PathVariable Integer employeeId) {
		MessageDTO message = new MessageDTO();
		try {
			message = attendanceService.checkIn(employeeId);
		} catch (Exception e) {
			message.setMessage("Unknown Error: " + e.getMessage());
		}

		return ResponseEntity.ok(message);
	}

	@PutMapping("/attendance/{employeeId}")
	public ResponseEntity<?> checkOut(@Valid @PathVariable Integer employeeId) {
		MessageDTO message = new MessageDTO();
		try {
			attendanceService.checkOut(employeeId);
			message.setMessage("Check Out Successfully");
		} catch (Exception e) {
			message.setMessage("Unknown Error: " + e.getMessage());
		}

		return ResponseEntity.ok(message);
	}

}
