package com.activpaisa.loan_app.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "organizations")
@Getter
@Setter
public class Organization {

    @Id
    @Column(name = "org_id", length = 50)
    private String id;

    @ManyToOne
    @JoinColumn(name = "parent_org_id")
    private Organization parentOrg;

    @OneToMany(mappedBy = "parentOrg")
    private List<Organization> childOrgs;

    @Column(name = "org_name", nullable = false)
    private String name;

    @Column(name = "org_code", unique = true)
    private String code;

    @Column(name = "org_type")
    private String orgType;

    @Column(name = "entity_type")
    private String entityType;

    @ManyToOne
    @JoinColumn(name = "partner_type_id")
    private PartnerType partnerType;

    @Column(name = "status")
    private String status = "ACTIVE";

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "organization")
    private List<User> users;

    @OneToMany(mappedBy = "organization")
    private List<Lead> leads;

    @OneToMany(mappedBy = "organization")
    private List<Role> roles;

    @OneToMany(mappedBy = "organization")
    private List<Department> departments;

    @OneToMany(mappedBy = "organization")
    private List<Team> teams;

    @OneToMany(mappedBy = "organization")
    private List<EmployeeProfile> employees;

    @OneToMany(mappedBy = "organization")
    private List<Lender> lenders;

    // Getters & Setters
}
