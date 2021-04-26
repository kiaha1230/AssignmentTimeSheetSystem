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

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "attendance")
@Getter
@Setter
@NoArgsConstructor
public class AttendanceEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "check_in_time")
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date checkInTime;

	@Column(name = "check_out_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date checkOutTime;

	@Column(name = "employee_id")
	private int employeeId;
}
