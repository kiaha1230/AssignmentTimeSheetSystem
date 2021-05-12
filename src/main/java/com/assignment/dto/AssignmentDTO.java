package com.assignment.dto;

import javax.validation.constraints.NotNull;

import com.assignment.entity.AssignmentEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel(description = "Class reprensenting an assignment for an employee to a project")
@Getter
@Setter
@NoArgsConstructor
public class AssignmentDTO {

	@ApiModelProperty(notes = "Unique identifier of the assignment", required = false, example = "1")
	private Integer id;

	@ApiModelProperty(notes = "Description of the assignment", required = false, example = "No Lanterns. No Kryptonian. This world will fall, like all the others")
	private String description;

	@ApiModelProperty(notes = "Unique identifier employee", required = true, example = "1")
	@NotNull(message = "error.blank")
	private Integer employeeId;

	@ApiModelProperty(notes = "Unique identifier of project", required = true, example = "1")
	@NotNull(message = "error.blank")
	private Integer projectId;

	public AssignmentDTO(AssignmentEntity entity) {
		this.id = entity.getId();
		this.description = entity.getDescription();
		this.employeeId = entity.getEmployeeId();
		this.projectId = entity.getProjectId();
	}

}
