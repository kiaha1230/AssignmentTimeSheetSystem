package com.assignment.repository;

import com.assignment.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer> {
    EmployeeEntity findEmployeeEntityByUsername(String username);

    @Query("UPDATE EmployeeEntity e SET e.failedAttempt = ?1 WHERE e.username = ?2")
    @Modifying
    void updateFailedAttempts(int failAttempts, String username);
}
