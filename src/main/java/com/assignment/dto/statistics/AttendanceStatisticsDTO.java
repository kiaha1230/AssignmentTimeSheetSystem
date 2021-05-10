/**
 * 
 */
package com.assignment.dto.statistics;

import java.util.List;

import com.assignment.dto.AttendanceDTO;

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
public class AttendanceStatisticsDTO {
	private String name;

	private List<AttendanceDTO> lsAttandance;

	private Integer lateDays;

	private Integer leaveEarlyDays;

	private Integer leaveDays;

	private int year;

	private int month;
}
