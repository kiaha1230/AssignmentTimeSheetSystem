package com.assignment.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestAPI {
	@RequestMapping("/hw")
	public String test() {
		return "hewwo";
	}

}
