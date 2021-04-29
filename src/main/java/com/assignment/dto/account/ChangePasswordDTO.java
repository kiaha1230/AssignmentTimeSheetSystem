package com.assignment.dto.account;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ChangePasswordDTO {
    @NotBlank(message = "error.blank")
    private String currentPassword;
    @NotBlank
    private String newPassword;
    @NotBlank
    private String retypePassword;



}
