/**
 * 
 */
package com.assignment.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.assignment.entity.OvertimeEntity;

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
public class OvertimeDTO {
	private Integer id;

	private String title;

	private Integer status;

	private String description;

	private Date date;

	@NotNull
	private Integer employeeId;

	public OvertimeDTO(OvertimeEntity entity) {
		this.id = entity.getId();
		this.title = entity.getTitle();
		this.status = entity.getStatus();
		this.description = entity.getDescription();
		this.date = entity.getDate();
		this.employeeId = entity.getEmployeeId();
	}
}
