package com.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assignment.entity.AssignmentEntity;

public interface AssignmentRepository extends JpaRepository<AssignmentEntity, Integer> {

}
