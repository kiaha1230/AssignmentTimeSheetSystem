package com.assignment.service;

import com.assignment.entity.EmployeeEntity;
import com.assignment.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;

@Service
@Transactional
public class EmployeeService {
    private EmployeeRepository employeeRepository;
    public static final int MAX_FAILED_ATTEMPTS = 3;
    private static final long LOCK_TIME_DURATION =10 * 1000; // 10s

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
    public void save(EmployeeEntity e){
        this.employeeRepository.save(e);
    }

    public int getMaxFailedAttempts(String username){
        EmployeeEntity e = this.employeeRepository.findEmployeeEntityByUsername(username);
        return e.getFailedAttempt();
    }

    public void increaseFailedAttempts(EmployeeEntity e) {
        int newFailAttempts = e.getFailedAttempt() + 1;
        this.employeeRepository.updateFailedAttempts(newFailAttempts, e.getUsername());
    }

    public void resetFailedAttempts(String username) {
        this.employeeRepository.updateFailedAttempts(0, username);
    }

    public void lock(EmployeeEntity e) {
        e.setAccountNonLocked(false);
        e.setLockTime(new Date());

        employeeRepository.save(e);
    }

    public boolean unlockWhenTimeExpired(EmployeeEntity e) {
        long lockTimeInMillis = e.getLockTime().getTime();
        long currentTimeInMillis = System.currentTimeMillis();
        if (lockTimeInMillis + LOCK_TIME_DURATION < currentTimeInMillis) {
            e.setAccountNonLocked(true);
            e.setLockTime(null);
            e.setFailedAttempt(0);
            employeeRepository.save(e);
            return true;
        }
        return false;
    }
}
