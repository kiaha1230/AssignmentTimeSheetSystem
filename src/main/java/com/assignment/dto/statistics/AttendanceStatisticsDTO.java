/**
 * 
 */
package com.assignment.dto.statistics;

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
	private int lateDays;

	private int soonDays;

	private int leaveDates;
}
