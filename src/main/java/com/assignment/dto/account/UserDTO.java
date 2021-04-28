package com.assignment.dto.account;

import com.assignment.enumm.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDTO {
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
