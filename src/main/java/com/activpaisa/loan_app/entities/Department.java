package com.activpaisa.loan_app.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "departments")
@Getter
@Setter
public class Department {

    @Id
    @Column(name = "department_id", length = 50)
    private String id;

    @ManyToOne
    @JoinColumn(name = "org_id")
    private Organization organization;

    @Column(name = "department_name")
    private String name;

    @OneToMany(mappedBy = "department")
    private List<EmployeeProfile> employees;

    // Getters & Setters
}