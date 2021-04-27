package com.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assignment.entity.EmployeeEntity;
import com.assignment.entity.OvertimeEntity;

public interface OvertimeRepository extends JpaRepository<OvertimeEntity, Integer> {

}
