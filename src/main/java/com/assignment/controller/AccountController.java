package com.assignment.controller;
import com.assignment.dto.account.*;
import com.assignment.entity.EmployeeEntity;
import com.assignment.jwt.JWTTokenComponent;
import com.assignment.jwt.JWTUserDetailsService;
import com.assignment.service.EmployeeService;
import com.assignment.transform.UserTransform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.DateFormat;
import java.util.Locale;

@RestController
@RequestMapping("api/accounts")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AccountController {
    private DateFormat dateFormat;
    private EmployeeService employeeService;
    private BCryptPasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JWTTokenComponent jwtTokenComponent;
    private JWTUserDetailsService jwtUserDetailsService;
    @Autowired
    public AccountController(DateFormat dateFormat, EmployeeService employeeService, BCryptPasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JWTTokenComponent jwtTokenComponent, JWTUserDetailsService jwtUserDetailsService) {
        this.dateFormat = dateFormat;
        this.employeeService = employeeService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenComponent = jwtTokenComponent;
        this.jwtUserDetailsService = jwtUserDetailsService;
    }
    @PostMapping("/login")
    public ResponseEntity<JWTResponseDTO> authenticate(@RequestBody AuthenticationRequestDTO dto){
        System.out.println(dto.getUsername());
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(dto.getUsername());
        String token = jwtTokenComponent.generateToken(userDetails);
        return ResponseEntity.ok(new JWTResponseDTO(token));
    }
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid CreateUserDTO body, Locale locale){
        UserTransform transform = new UserTransform(dateFormat);
        EmployeeEntity employeeEntity = transform.apply(body);
        encryptPassword(employeeEntity);
        employeeService.save(employeeEntity);
        return ResponseEntity.ok(transform.apply(employeeEntity));
    }
    private void encryptPassword(EmployeeEntity employeeEntity) {
        String rawPassword = employeeEntity.getPassword();
        if (rawPassword != null) {
            employeeEntity.setPassword(passwordEncoder.encode(rawPassword));
        }
    }
}
