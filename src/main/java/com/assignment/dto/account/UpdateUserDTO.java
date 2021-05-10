package com.assignment.dto.account;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDTO {
    private String email;
    private String fullName;
    private String phoneNumber;
    private String address;
    private String bankAccount;
    private String bankName;
}
