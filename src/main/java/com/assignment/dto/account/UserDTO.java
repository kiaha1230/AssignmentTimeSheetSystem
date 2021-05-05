package com.assignment.dto.account;

import com.assignment.enumm.Role;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class UserDTO {
	@NotBlank(message = "error.blank")
    @Size(min = 10, max = 20, message = "size.username")
    private String username;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String address;
    private String bankAccount;
    private String bankName;
    private boolean active;
    private List<Role> roles;
}
