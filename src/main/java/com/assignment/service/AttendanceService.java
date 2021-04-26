package com.assignment.service;

import javax.persistence.Entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assignment.entity.AttendanceEntity;
import com.assignment.repository.AttendanceRepository;

@Service
public class AttendanceService {
	@Autowired
	AttendanceRepository attendanceRepository;

}
