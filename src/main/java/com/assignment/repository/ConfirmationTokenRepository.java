package com.assignment.repository;

import com.assignment.entity.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Integer> {
    ConfirmationToken findByToken(String token);
}
