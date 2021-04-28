package com.assignment.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "employee")
@Getter
@Setter
@NoArgsConstructor
public class EmployeeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "fullname")
	private String fullName;

	@Column(name = "phone_number")
	private String phoneNumber;

	@Column(name = "email")
	private String email;

	@Column(name = "address")
	private String address;

	@Column(name = "bank_account")
	private String bankAccount;

	@Column(name = "bank_name")
	private String bankName;

	@Column(name = "username")
	private String username;

	@Column(name = "password")
	private String password;

	@Column(name = "roles")
	private int roles;

	@Column(name = "active")
	private boolean active;

//	public EmployeeEntity(EmployeeDTO dto) {
//		this.id = dto.getId();
//		this.fullName = dto.getFullName();
//		this.phoneNumber = dto.getPhoneNumber();
//		this.email = dto.getEmail();
//		this.address = dto.getAddress();
//		this.bankAccount = dto.getBankAccount();
//		this.bankName = dto.getBankName();
//		this.username = dto.getUsername();
//		this.password = dto.getPassword();
//		this.isAdmin = dto.getIsAdmin();
//		this.isSalAccountancy = dto.getIsSalAccountancy();
//	}

}
