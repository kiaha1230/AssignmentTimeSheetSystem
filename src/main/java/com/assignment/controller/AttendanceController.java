/**
 * 
 */
package com.assignment.controller;

import java.util.List;

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

import com.assignment.dto.MessageDTO;
import com.assignment.dto.TimeDTO;
import com.assignment.dto.statistics.AttendanceStatisticsDTO;
import com.assignment.service.AttendanceService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author baovd
 *
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Api(description = "Endpoints for Check In, Check Out and view, export Excel of employee attendance", tags = { "Attendance" })
public class AttendanceController {
	@Autowired
	private AttendanceService attendanceService;

	@ApiOperation(value = "API Check In", notes = " Remember to Check In every time you arrive office")
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

	@ApiOperation(value = "API Check Out", notes = " Remember to Check Out every time you leave")
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

	@ApiOperation(value = "API view all Attendance Statistics", notes = "Only Admin and Salary Accountancy can use this API")
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

	@ApiOperation(value = "API view personal Attandance Statistics", notes = "Every employee can use this API ")
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

	@ApiOperation(value = "Export to Excel file ", notes = "more comfortable, easy to use")
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
