package com.assignment.entity;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "confirmation_token")
@Getter
@Setter

public class ConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "token")
    private String token;
    @Column(name="created_date")
    private Date createdDate;
    @OneToOne(targetEntity = EmployeeEntity.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "employee_id")
    private EmployeeEntity employeeEntity;

    public ConfirmationToken() {
    }

    public ConfirmationToken(EmployeeEntity employeeEntity) {
        this.employeeEntity = employeeEntity;
        createdDate = new Date();
        token = UUID.randomUUID().toString();
    }
}
