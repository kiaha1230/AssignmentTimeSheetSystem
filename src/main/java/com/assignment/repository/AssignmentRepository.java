package com.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assignment.entity.EmployeeEntity;

public interface AssignmentRepository extends JpaRepository<EmployeeEntity, Integer> {

}
