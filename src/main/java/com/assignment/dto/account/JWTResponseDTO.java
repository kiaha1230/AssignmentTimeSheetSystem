package com.assignment.dto.account;

public class JWTResponseDTO {

    private String token;

    public JWTResponseDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}

