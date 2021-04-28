package com.assignment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Configuration
public class AppConfiguration {

    @Bean
    public DateFormat dateFormat() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        return format;
    }
}
