package com.assignment.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TestController {
    @GetMapping
    @Secured("ROLE_ADMIN")
    public String hello(){
        return "HEllo";
    }
}
