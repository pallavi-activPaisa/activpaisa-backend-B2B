package com.activpaisa.loan_app.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "permissions")
@Getter
@Setter
public class Permission {

    @Id
    @Column(name = "permission_id", length = 50)
    private String id;

    @Column(name = "permission_key", nullable = false, unique = true)
    private String key;

    @Column
    private String description;

    @ManyToMany(mappedBy = "permissions")
    private List<Role> roles;

    // Getters & Setters
}