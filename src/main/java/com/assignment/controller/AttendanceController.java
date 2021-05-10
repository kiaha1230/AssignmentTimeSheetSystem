/**
 * 
 */
package com.assignment.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.dto.AttendanceDTO;
import com.assignment.dto.MessageDTO;
import com.assignment.dto.TimeDTO;
import com.assignment.dto.statistics.AttendanceStatisticsDTO;
import com.assignment.service.AttendanceService;

/**
 * @author baovd
 *
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
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
			message = attendanceService.checkOut(employeeId);
		} catch (Exception e) {
			message.setMessage("Unknown Error: " + e.getMessage());
		}

		return ResponseEntity.ok(message);
	}

	@PostMapping("/all-attendance-statistics")
	public ResponseEntity<?> viewAllStatistics(@Valid @RequestBody TimeDTO timeDTO) {
		MessageDTO message = new MessageDTO();
		try {
			List<AttendanceStatisticsDTO> ls = attendanceService.allAttendanceStatistics(timeDTO.getYear(),
					timeDTO.getMonth());
			return ResponseEntity.ok(ls);
		} catch (Exception e) {
			message.setMessage("Unknown Error: " + e.getMessage());
		}
		return ResponseEntity.ok(message);
	}

	@PostMapping("/attendance-statistics")
	public ResponseEntity<?> viewPersonalStatistics(@Valid @RequestBody TimeDTO timeDTO) {
		MessageDTO message = new MessageDTO();
		try {
			AttendanceStatisticsDTO statisticDTO = attendanceService.personalAttendanceStatistics(timeDTO.getYear(),
					timeDTO.getMonth());
			return ResponseEntity.ok(statisticDTO);
		} catch (Exception e) {
			message.setMessage("Unknown Error: " + e.getMessage());
		}
		return ResponseEntity.ok(message);
	}

	@PostMapping("attendance-statistics/export")
	public ResponseEntity<?> exportPersonal(@Valid @RequestBody TimeDTO timeDTO) {
		MessageDTO message = new MessageDTO();
		try {
			attendanceService.exportExcel(timeDTO.getYear(), timeDTO.getMonth());
			return ResponseEntity.ok("ok");
		} catch (Exception e) {
			message.setMessage("Unknown Error: " + e.getMessage());
		}
		return ResponseEntity.ok(message);
	}

}
