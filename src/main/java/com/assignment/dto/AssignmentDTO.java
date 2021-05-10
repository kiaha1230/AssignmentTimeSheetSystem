package com.assignment.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.assignment.entity.AssignmentEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AssignmentDTO {
	private Integer id;

	private String description;

	@NotNull(message ="error.blank" )
	private Integer employeeId;

	@NotNull(message = "error.blank")
	private Integer projectId;

	public AssignmentDTO(AssignmentEntity entity) {
		this.id = entity.getId();
		this.description = entity.getDescription();
		this.employeeId = entity.getEmployeeId();
		this.projectId = entity.getProjectId();
	}

}
