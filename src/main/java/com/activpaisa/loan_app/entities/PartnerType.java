package com.activpaisa.loan_app.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "partner_types")
@Getter
@Setter
public class PartnerType {

    @Id
    @Column(name = "partner_type_id", length = 50)
    private String id;

    @Column(name = "partner_type_name", nullable = false, unique = true)
    private String name;

    @Column(name = "hierarchy_level")
    private Integer hierarchyLevel;

    @Column
    private String description;

    @Column(name = "status")
    private String status = "ACTIVE";

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "partnerType")
    private List<Organization> organizations;

    // Getters & Setters
}