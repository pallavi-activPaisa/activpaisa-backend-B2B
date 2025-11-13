package com.activpaisa.loan_app.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "user_types")
@Getter
@Setter
public class UserType {

    @Id
    @Column(name = "user_type_id", length = 50)
    private String id;

    @Column(name = "user_type_name", nullable = false, unique = true)
    private String name;

    @Column
    private String description;

    @ManyToMany(mappedBy = "userTypes")
    private List<Portal> portals;

    @OneToMany(mappedBy = "userType")
    private List<User> users;

    // Getters & Setters
}