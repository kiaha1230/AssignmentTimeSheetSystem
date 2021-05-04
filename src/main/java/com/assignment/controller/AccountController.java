package com.assignment.controller;
import com.assignment.dto.MessageDTO;
import com.assignment.dto.account.*;
import com.assignment.entity.EmployeeEntity;
import com.assignment.jwt.JWTTokenComponent;
import com.assignment.jwt.JWTUserDetailsService;
import com.assignment.service.EmployeeService;
import com.assignment.transform.UserTransform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.validation.Valid;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Locale;

@RestController
@RequestMapping("api/accounts")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AccountController {
    private MessageSource messageSource;
    private DateFormat dateFormat;
    private EmployeeService employeeService;
    private BCryptPasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JWTTokenComponent jwtTokenComponent;
    private JWTUserDetailsService jwtUserDetailsService;
    @Autowired
    public AccountController(MessageSource messageSource, DateFormat dateFormat, EmployeeService employeeService, BCryptPasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JWTTokenComponent jwtTokenComponent, JWTUserDetailsService jwtUserDetailsService) {
        this.messageSource = messageSource;
        this.dateFormat = dateFormat;
        this.employeeService = employeeService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenComponent = jwtTokenComponent;
        this.jwtUserDetailsService = jwtUserDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequestDTO body) {
        EmployeeEntity e = employeeService.findByUserName(body.getUsername());
        MessageDTO response  = new MessageDTO();
        if(e!=null){
            //if account isn't locked
            if(e.isAccountNonLocked()){
                if(passwordEncoder.matches(body.getPassword(), e.getPassword())){
                    employeeService.resetFailedAttempts(body.getUsername());
                    UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(body.getUsername());
                    String token = jwtTokenComponent.generateToken(userDetails);
                    return ResponseEntity.ok(new JWTResponseDTO(token));
                } else {
                    if(e.getFailedAttempt() < employeeService.MAX_FAILED_ATTEMPTS-1){
                        employeeService.increaseFailedAttempts(e);
                        int failedTimes = e.getFailedAttempt() + 1;
                        response.setMessage("login failed "+ failedTimes + " times");
                        return ResponseEntity.badRequest().body(response);
                    }
                    employeeService.lock(e);
                    response.setMessage("Account is locked");
                    return ResponseEntity.badRequest().body(response);
                }
            //if account is locked and Time expired -> unlock and login
            } else{
               if(employeeService.unlockWhenTimeExpired(e)){
                   if(passwordEncoder.matches(body.getPassword(), e.getPassword())){
                       employeeService.resetFailedAttempts(body.getUsername());
                       UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(body.getUsername());
                       String token = jwtTokenComponent.generateToken(userDetails);
                       return ResponseEntity.ok(new JWTResponseDTO(token));
                   } else {
                       if(e.getFailedAttempt() < employeeService.MAX_FAILED_ATTEMPTS - 1 ){
                           employeeService.increaseFailedAttempts(e);
                           int failedTimes = e.getFailedAttempt() + 1;
                           response.setMessage("login failed "+ failedTimes + " times");
                           return ResponseEntity.badRequest().body(response);
                       }
                       employeeService.lock(e);
                       response.setMessage("Account is locked");
                       return ResponseEntity.badRequest().body(response);

                   }
               //return account is loked
               } else {
                   response.setMessage("Account is locked");
                   return ResponseEntity.badRequest().body(response);
               }
            }
        } else{
            // return Account doesn't exist
            response.setMessage("Account doesn't exist");
            return ResponseEntity.badRequest().body(response);
        }
    }
    @PostMapping
    public ResponseEntity<MessageDTO> createUser(@RequestBody @Valid CreateUserDTO body, Locale locale){
        UserTransform transform = new UserTransform(dateFormat);
        EmployeeEntity employeeEntity = transform.apply(body);
        MessageDTO response = new MessageDTO();
        System.out.println(RegexValidator.isValidEmail(body.getEmail()));
        if(employeeService.findByUserName(body.getUsername())==null){
            if(RegexValidator.isValidEmail(body.getEmail()) && RegexValidator.isValidPassWord(body.getPassword())) {
                encryptPassword(employeeEntity);
                employeeEntity.setAccountNonLocked(true);
                employeeService.save(employeeEntity);
                response.setMessage(messageSource.getMessage("success.created", null, locale));
                return ResponseEntity.ok(response);
            } else{
                response.setMessage(messageSource.getMessage("error.emailpassword", null, locale));
                return ResponseEntity.badRequest().body(response);
            }
        } else{
            response.setMessage(messageSource.getMessage("error.created", null, locale));
            return ResponseEntity.badRequest().body(response);
        }


    }
    @PutMapping("/password")
    public ResponseEntity<MessageDTO> changePassword(@RequestBody @Valid ChangePasswordDTO body, Locale locale) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        String username = securityContext.getAuthentication().getName();
        MessageDTO response = new MessageDTO();
        if(!body.getCurrentPassword().equals(body.getNewPassword())){
            if(body.getNewPassword().equals(body.getRetypePassword())){
                if(RegexValidator.isValidPassWord(body.getNewPassword())){
                    EmployeeEntity employeeEntity = employeeService.findByUserName(username);
                    if (passwordEncoder.matches(body.getCurrentPassword(), employeeEntity.getPassword())) {
                        employeeEntity.setPassword(body.getNewPassword());
                        encryptPassword(employeeEntity);
                        employeeService.save(employeeEntity);
                        response.setMessage(messageSource.getMessage("success.pwd.passwordChanged", null, locale));
                        return ResponseEntity.ok(response);
                    } else {
                        response.setMessage(messageSource.getMessage("error.pwd.currentPassword", null, locale));
                        return ResponseEntity.badRequest().body(response);
                    }
                } else {
                    response.setMessage(messageSource.getMessage("error.pwd.format", null, locale));
                    return ResponseEntity.badRequest().body(response);
                }
            }else {
                response.setMessage(messageSource.getMessage("error.pwd.New&Retype", null, locale));
                return ResponseEntity.badRequest().body(response);
            }
        } else {
            response.setMessage(messageSource.getMessage("error.pwd.CurrentPwd&NewPwd", null, locale));
            return ResponseEntity.badRequest().body(response);
        }
    }
    @PutMapping
    public ResponseEntity<MessageDTO> updateAccount(@RequestBody @Valid UpdateUserDTO body,Locale locale){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        String username = securityContext.getAuthentication().getName();
        MessageDTO response = new MessageDTO();
        EmployeeEntity employeeEntity = employeeService.findByUserName(username);
        employeeEntity.setEmail(body.getEmail());
        employeeEntity.setFullName(body.getFullName());
        employeeEntity.setBankAccount(body.getBankAccount());
        employeeEntity.setBankName(body.getBankName());
        employeeEntity.setPhoneNumber(body.getPhoneNumber());
        employeeEntity.setAddress(body.getAddress());
        employeeService.save(employeeEntity);
        response.setMessage(messageSource.getMessage("success.update",null,locale));
        return ResponseEntity.ok(response);
    }
    private void encryptPassword(EmployeeEntity employeeEntity) {
        String rawPassword = employeeEntity.getPassword();
        if (rawPassword != null) {
            employeeEntity.setPassword(passwordEncoder.encode(rawPassword));
        }
    }
//    @PostMapping("/password/forgot")
//    public ResponseEntity<MessageDTO> reset(){
//        return
//    }

}
