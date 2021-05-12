package com.assignment.controller;
import com.assignment.dto.MessageDTO;
import com.assignment.dto.account.*;
import com.assignment.entity.ConfirmationToken;
import com.assignment.entity.EmployeeEntity;
import com.assignment.jwt.JWTTokenComponent;
import com.assignment.jwt.JWTUserDetailsService;
import com.assignment.repository.ConfirmationTokenRepository;
import com.assignment.service.EmailSenderService;
import com.assignment.service.EmployeeService;
import com.assignment.transform.UserTransform;
import io.jsonwebtoken.impl.DefaultClaims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
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
    private ConfirmationTokenRepository confirmationTokenRepository;
    private EmailSenderService emailSenderService;

    @Autowired
    public AccountController(MessageSource messageSource, DateFormat dateFormat, EmployeeService employeeService, BCryptPasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JWTTokenComponent jwtTokenComponent, JWTUserDetailsService jwtUserDetailsService, ConfirmationTokenRepository confirmationTokenRepository, EmailSenderService emailSenderService) {
        this.messageSource = messageSource;
        this.dateFormat = dateFormat;
        this.employeeService = employeeService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenComponent = jwtTokenComponent;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.emailSenderService = emailSenderService;
    }
    @ApiOperation(value = "API login, return token if success")
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
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
                    }
                    employeeService.lock(e);
                    response.setMessage("Account is locked, please wait 10s to try again");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
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
                           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
                       }
                       employeeService.lock(e);
                       response.setMessage("Account is locked");
                       return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

                   }
               //return account is loked
               } else {
                   response.setMessage("Account is locked, please wait 10s to try again");
                   return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
               }
            }
        } else{
            // return Account doesn't exist
            response.setMessage("Account doesn't exist");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
    @ApiOperation(value = "API create user, only admin can create new user")
    @PostMapping
    @Secured("ROLE_ADMIN")
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
                response.setMessage(messageSource.getMessage("error.email-password", null, locale));
                return ResponseEntity.badRequest().body(response);
            }
        } else{
            response.setMessage(messageSource.getMessage("error.user.created", null, locale));
            return ResponseEntity.badRequest().body(response);
        }
    }
    @ApiOperation(value="API change password")
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
    @ApiOperation(value = "API update information account")
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

    @ApiOperation(value = "API send email to reset password")
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordDTO body, Locale locale){
        EmployeeEntity e = employeeService.findByUserName(body.getUsername());
        MessageDTO response = new MessageDTO();
        if(e!=null){
            ConfirmationToken confirmationToken = new ConfirmationToken(e);
            this.confirmationTokenRepository.save(confirmationToken);
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(e.getEmail());
            mailMessage.setSubject("Reset Password - TimeSheet Application");
            mailMessage.setText("To complete the password reset process, please click here: "
                    + "http://localhost:8080/api/accounts/confirm-reset?token="+confirmationToken.getToken());
            emailSenderService.sendEmail(mailMessage);
            response.setMessage("Request to reset password received. Check your inbox for the reset link");
            response.setMessage(messageSource.getMessage("reset.password.email",null,locale));
            return ResponseEntity.ok(response);
        } else {
            response.setMessage("This username does not exist");
            return ResponseEntity.badRequest().body(response);
        }
    }
    @ApiOperation(value = "API check reset password token is valid")
    @PostMapping("/confirm-reset")
    public ResponseEntity<?> validateResetToken(@RequestParam("token") String token){
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token);
        if(confirmationToken!=null){
            ResetTokenDTO resetTokenDTO = new ResetTokenDTO();
            resetTokenDTO.setResetToken(token);
            return ResponseEntity.ok(resetTokenDTO);
        } else{
            MessageDTO response = new MessageDTO();
            response.setMessage("Reset token is invalid");
            return ResponseEntity.badRequest().body(response);
        }
    }
    @ApiOperation(value = "api reset password after get email reset password")
    @PutMapping("/reset-password")
    public ResponseEntity<MessageDTO> resetPassword(@RequestBody ResetPasswordDTO body, Locale locale){
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(body.getResetToken());
        MessageDTO response = new MessageDTO();
        if(confirmationToken!=null) {
            if(body.getNewPassword().equals(body.getRetypePassword())){
                if(RegexValidator.isValidPassWord(body.getNewPassword())){
                    EmployeeEntity e = confirmationToken.getEmployeeEntity();
                    e.setPassword(passwordEncoder.encode(body.getNewPassword()));
                    employeeService.save(e);
                    response.setMessage("Reset password success");
                    return ResponseEntity.ok(response);
                } else{
                    response.setMessage(messageSource.getMessage("error.pwd.format", null, locale));
                    return ResponseEntity.badRequest().body(response);
                }
            }else {
                response.setMessage("Retype new password not the same new password");
                return ResponseEntity.ok(response);
            }

        } else {
            response.setMessage("Reset token is invalid");
            return ResponseEntity.badRequest().body(response);
        }
    }
//    @RequestMapping(value = "/refreshtoken", method = RequestMethod.GET)
//    public ResponseEntity<?> refreshtoken(HttpServletRequest request) throws Exception {
//        // From the HttpRequest get the claims
//        DefaultClaims claims = (io.jsonwebtoken.impl.DefaultClaims) request.getAttribute("claims");
//
//        Map<String, Object> expectedMap = getMapFromIoJsonwebtokenClaims(claims);
//        String token = jwtTokenComponent.doGenerateRefreshToken(expectedMap, expectedMap.get("sub").toString());
//        return ResponseEntity.ok(new AuthenticationResponseDTO(token));
//    }
//
//    public Map<String, Object> getMapFromIoJsonwebtokenClaims(DefaultClaims claims) {
//        Map<String, Object> expectedMap = new HashMap<String, Object>();
//        for (Entry<String, Object> entry : claims.entrySet()) {
//            expectedMap.put(entry.getKey(), entry.getValue());
//        }
//        return expectedMap;
//    }
    private void encryptPassword(EmployeeEntity employeeEntity) {
        String rawPassword = employeeEntity.getPassword();
        if (rawPassword != null) {
            employeeEntity.setPassword(passwordEncoder.encode(rawPassword));
        }
    }
}
