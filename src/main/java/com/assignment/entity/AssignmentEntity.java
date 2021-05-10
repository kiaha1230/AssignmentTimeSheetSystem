package com.assignment.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.assignment.dto.AssignmentDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "assignment")
@Getter
@Setter
@NoArgsConstructor
public class AssignmentEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "description")
	private String description;

	@Column(name = "employee_id")
	private Integer employeeId;

	@Column(name = "project_id")
	private Integer projectId;

	public AssignmentEntity(AssignmentDTO dto) {
		this.id = dto.getId();
		this.description = dto.getDescription();
		this.employeeId = dto.getEmployeeId();
		this.projectId = dto.getProjectId();
	}
}
