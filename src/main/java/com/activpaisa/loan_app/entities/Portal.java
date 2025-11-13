package com.activpaisa.loan_app.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "portals")
@Getter
@Setter
public class Portal {

    @Id
    @Column(name = "portal_id", length = 50)
    private String id;

    @Column(name = "portal_name", nullable = false, unique = true)
    private String name;

    @Column(name = "base_url")
    private String baseUrl;

    @OneToMany(mappedBy = "portal")
    private List<User> users;

    @ManyToMany
    @JoinTable(
        name = "portal_user_types",
        joinColumns = @JoinColumn(name = "portal_id"),
        inverseJoinColumns = @JoinColumn(name = "user_type_id")
    )
    private List<UserType> userTypes;

    @ManyToMany(mappedBy = "portals")
    private List<Role> roles;

    // Getters & Setters
}