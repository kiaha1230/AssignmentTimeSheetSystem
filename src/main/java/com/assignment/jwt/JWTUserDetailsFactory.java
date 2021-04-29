package com.assignment.jwt;

import com.assignment.entity.EmployeeEntity;
import com.assignment.enumm.Role;
import com.assignment.transform.RoleTransform;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

public class JWTUserDetailsFactory {
    private JWTUserDetailsFactory() {
    }

    public static JWTUserDetails create(EmployeeEntity employeeEntity) {
        return new JWTUserDetails(
                employeeEntity.getId(),
                employeeEntity.getUsername(),
                employeeEntity.getPassword(),
                employeeEntity.getEmail(),
                mapToGrantedAuthorities(employeeEntity.getRoles()),
                employeeEntity.isActive(),
                employeeEntity.isAccountNonLocked()
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(int rolesNumber) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        RoleTransform transform = new RoleTransform();
        List<Role> roles = transform.apply(rolesNumber);
        for (Role role: roles) {
            authorities.add(new SimpleGrantedAuthority(role.toString()));
        }
        return authorities;
    }
}
