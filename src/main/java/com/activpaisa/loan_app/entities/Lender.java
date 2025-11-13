package com.activpaisa.loan_app.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "lenders")
@Getter
@Setter
public class Lender {

    @Id
    @Column(name = "lender_id", length = 50)
    private String id;

    @ManyToOne
    @JoinColumn(name = "org_id")
    private Organization organization;

    @Column
    private String name;

    @ManyToOne
    @JoinColumn(name = "lender_type_id")
    private LenderType lenderType;

    @Column(name = "status")
    private String status = "ACTIVE";

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "lender")
    private List<Product> products;

    // Getters & Setters
}