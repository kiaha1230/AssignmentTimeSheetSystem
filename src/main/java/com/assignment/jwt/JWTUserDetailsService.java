package com.assignment.jwt;

import com.assignment.entity.EmployeeEntity;
import com.assignment.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class JWTUserDetailsService implements UserDetailsService {

    private EmployeeService employeeService;

    @Autowired
    public JWTUserDetailsService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            EmployeeEntity employeeEntity = employeeService.findByUserName(username);
            return JWTUserDetailsFactory.create(employeeEntity);
        } catch (Exception e) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username), e);
        }
    }
}
