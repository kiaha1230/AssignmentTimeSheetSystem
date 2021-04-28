package com.assignment.repository;

import com.assignment.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer> {
    EmployeeEntity findEmployeeEntityByUsername(String username);
}
