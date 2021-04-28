package com.assignment.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {

	@org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> validationDto(MethodArgumentNotValidException exception) {
		 Map<String, String> errors = new HashMap<>();
		    exception.getBindingResult().getFieldErrors().forEach((error) -> {
		        String fieldName =error.getField();
		        String errorMessage = error.getDefaultMessage();
		        errors.put(fieldName, errorMessage);
		    });
			return ResponseEntity.ok(errors);
	}
}
