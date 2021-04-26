package com.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assignment.entity.AttendanceEntity;

public interface AttendanceRepository extends JpaRepository<AttendanceEntity, Integer> {

}
