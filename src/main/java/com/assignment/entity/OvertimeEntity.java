package com.assignment.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

import com.assignment.dto.DecideOTRequestDTO;
import com.assignment.dto.OvertimeDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "overtime")
@Getter
@Setter
@NoArgsConstructor
public class OvertimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "title")
	private String title;

	@Column(name = "status")
	private Integer status;

	@Column(name = "description")
	private String description;

	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	@Column(name = "date")
	private Date date;

	@Column(name = "employee_id")
	private Integer employeeId;

	public OvertimeEntity(OvertimeDTO dto) {
		this.id = dto.getId();
		this.title = dto.getTitle();
		this.status = dto.getStatus();
		this.description = dto.getDescription();
		this.date = dto.getDate();
		this.employeeId = dto.getEmployeeId();
	}
	public OvertimeEntity(DecideOTRequestDTO dto) {
		this.id = dto.getId();
		this.status = dto.getStatus();
	}
}
