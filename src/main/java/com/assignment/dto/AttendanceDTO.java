/**
 * 
 */
package com.assignment.dto;

import java.time.LocalDateTime;
import java.util.Date;

import com.assignment.entity.AttendanceEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author baovd
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class AttendanceDTO {

	private int id;

	private LocalDateTime checkInTime;

	private LocalDateTime checkOutTime;

	private int employeeId;
	
	private boolean late;
	
	private boolean leaveEarly;

	public AttendanceDTO(AttendanceEntity entity) {
		this.id = entity.getId();
		this.checkInTime = entity.getCheckInTime();
		this.checkOutTime = entity.getCheckOutTime();
		this.employeeId = entity.getEmployeeId();
	}
}
