/**
 * 
 */
package com.assignment.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.assignment.entity.OvertimeEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author baovd
 *
 */
@ApiModel(description = "Class reprensenting a overtime request ")
@Getter
@Setter
@NoArgsConstructor
public class OvertimeDTO {
	@ApiModelProperty(notes = "Unique identifier of the overtime request", required = false, example = "1")
	private int id;
	@ApiModelProperty(notes = "Title of the overtime request", required = false)
	private String title;
	@ApiModelProperty(notes = "Status of the ot request, default is 1:pending", required = false)
	private Integer status;
	@ApiModelProperty(notes = "description, notes to PM", required = false, example = "Need overtime to get a function update in time")
	private String description;
	@ApiModelProperty(notes = "create OT request date, get current time as default, do not modify", required = false)
	private Date date;
	@ApiModelProperty(notes = "employeeId who create OT request", required = true, example = "2")
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
