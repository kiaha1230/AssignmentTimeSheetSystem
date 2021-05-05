package com.assignment.dto.account;

public class AuthenticationResponseDTO {

	private String jwtToken;

	public AuthenticationResponseDTO() {
	}

	public AuthenticationResponseDTO(String jwtToken) {
		this.jwtToken = jwtToken;
	}

	public String getJwtToken() {
		return jwtToken;
	}

	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}

}
