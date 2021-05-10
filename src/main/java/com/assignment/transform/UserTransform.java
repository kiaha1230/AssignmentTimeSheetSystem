package com.assignment.transform;

import com.assignment.dto.account.CreateUserDTO;
import com.assignment.dto.account.UserDTO;
import com.assignment.entity.EmployeeEntity;

import java.text.DateFormat;

public class UserTransform {
    private DateFormat dateFormat;
    private RoleTransform roleTransform;

    public UserTransform(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
        roleTransform = new RoleTransform();
    }

    public EmployeeEntity apply(CreateUserDTO dto){
        EmployeeEntity employeeEntity = new EmployeeEntity();
        employeeEntity.setUsername(dto.getUsername());
        employeeEntity.setPassword(dto.getPassword());
        employeeEntity.setEmail(dto.getEmail());
        employeeEntity.setFullName(dto.getFullName());
        employeeEntity.setPhoneNumber(dto.getPhoneNumber());
        employeeEntity.setAddress(dto.getAddress());
        employeeEntity.setBankAccount(dto.getBankAccount());
        employeeEntity.setBankName(dto.getBankName());
        employeeEntity.setRoles(roleTransform.apply(dto.getRoles()));
        employeeEntity.setActive(true);
        return employeeEntity;
    }

    public UserDTO apply(EmployeeEntity employeeEntity){
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(employeeEntity.getUsername());
        userDTO.setEmail(employeeEntity.getEmail());
        userDTO.setBankAccount(employeeEntity.getBankAccount());
        userDTO.setBankName(employeeEntity.getBankName());
        userDTO.setFullName(employeeEntity.getFullName());
        userDTO.setRoles(roleTransform.apply(employeeEntity.getRoles()));
        return userDTO;
    }


}
