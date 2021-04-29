package com.assignment.service;

import com.assignment.entity.AssignmentEntity;
import com.assignment.entity.EmployeeEntity;
import com.assignment.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class EmployeeService {
    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Optional<EmployeeEntity> findById(int id){
        return this.employeeRepository.findById(id);
    }

    public EmployeeEntity findByUserName(String username){
        return this.employeeRepository.findEmployeeEntityByUsername(username);
    }
    @Transactional
    public void save(EmployeeEntity e){
        this.employeeRepository.save(e);
    }
}
