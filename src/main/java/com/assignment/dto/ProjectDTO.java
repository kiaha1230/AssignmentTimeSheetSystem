package com.assignment.dto;

import javax.validation.constraints.NotBlank;

import com.assignment.entity.ProjectEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel(description = "Class reprensenting a project in application ")
@Getter
@Setter
@NoArgsConstructor
public class ProjectDTO {
	@ApiModelProperty(notes = "Unique identifier of the project", required = false, example = "1")
	private int id;

	@ApiModelProperty(notes = "Name of the project", required = true, example = "Ready the Armada")
	@NotBlank(message = "error.blank")
	private String name;

	@ApiModelProperty(notes = "Description of the project", required = false, example = "Found the anti-life, we'll using the old ways")
	private String description;

	@ApiModelProperty(notes = "Status of the project, default is 1 : active", required = false, example = "1")
	private Integer status;

	@ApiModelProperty(notes = "EmployeeId, employee becomes PM", required = false, example = "1")	
	private Integer pmId;

	public ProjectDTO(ProjectEntity entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.description = entity.getDescription();
		this.status = entity.getStatus();
		this.pmId = entity.getPmId();
	}
}
