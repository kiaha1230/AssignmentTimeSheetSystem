/**
 * 
 */
package com.assignment.dto;

import java.util.Date;

import javax.persistence.Column;

import com.assignment.entity.EmployeeEntity;

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
public class EmployeeDTO {
	private int id;

	private String fullName;

	private String phoneNumber;

	private String email;

	private String address;

	private String bankAccount;

	private String bankName;

	private String username;

	private String password;

	private int roles;

	private boolean active;

	private boolean accountNonLocked;

	private int failedAttempt;

	private Date lockTime;

	public EmployeeDTO(EmployeeEntity entity) {
		this.id = entity.getId();
		this.fullName = entity.getFullName();
		this.phoneNumber = entity.getPhoneNumber();
		this.email = entity.getEmail();
		this.address = entity.getAddress();
		this.bankAccount = entity.getBankAccount();
		this.bankName = entity.getBankName();
		this.username = entity.getUsername();
		this.password = entity.getPassword();
		this.roles = entity.getRoles();
		this.active = entity.isActive();
		this.accountNonLocked = entity.isAccountNonLocked();
		this.failedAttempt = entity.getFailedAttempt();
		this.lockTime = entity.getLockTime();
	}
}
