package com.activpaisa.loan_app.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "employee_profiles")
@Getter
@Setter
public class EmployeeProfile {

    @Id
    @Column(name = "employee_id", length = 50)
    private String id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @ManyToOne
    @JoinColumn(name = "org_id")
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "designation_id")
    private Designation designation;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "reporting_manager_id")
    private User reportingManager;

    @Column(name = "join_date")
    private LocalDate joinDate;

    @Column(name = "status")
    private String status = "ACTIVE";

    // Getters & Setters
}