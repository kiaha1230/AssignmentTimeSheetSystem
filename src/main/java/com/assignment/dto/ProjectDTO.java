package com.assignment.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.assignment.entity.ProjectEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProjectDTO {
	private Integer id;
	@NotNull
	@NotBlank
	private String name;

	private String description;

	private Integer status;

	private Integer pmId;

	public ProjectDTO(ProjectEntity entity) {
		this.name = entity.getName();
		this.description = entity.getDescription();
		this.status = entity.getStatus();
		this.pmId = entity.getPmId();
	}
}
