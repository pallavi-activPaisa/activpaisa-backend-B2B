package com.activpaisa.loan_app.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
public class Product {

    @Id
    @Column(name = "product_id", length = 50)
    private String id;

    @ManyToOne
    @JoinColumn(name = "lender_id")
    private Lender lender;

    @Column(name = "product_name")
    private String name;

    @Column(name = "product_type")
    private String type;

    @Column(name = "status")
    private String status = "ACTIVE";

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "product")
    private List<ProductPolicy> policies;

    @OneToMany(mappedBy = "product")
    private List<CommissionScheme> commissionSchemes;

    // Getters & Setters
}