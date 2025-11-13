package com.activpaisa.loan_app.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "designations")
@Getter
@Setter
public class Designation {

    @Id
    @Column(name = "designation_id", length = 50)
    private String id;

    @Column(name = "designation_name")
    private String name;

    @Column
    private String description;

    @OneToMany(mappedBy = "designation")
    private List<EmployeeProfile> employees;

    // Getters & Setters
}