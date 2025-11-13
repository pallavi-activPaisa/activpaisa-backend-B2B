package com.activpaisa.loan_app.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "commission_schemes")
@Getter
@Setter
public class CommissionScheme {

    @Id
    @Column(name = "scheme_id", length = 50)
    private String id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "scheme_name")
    private String name;

    @Column(name = "scheme_value")
    private String value;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Getters & Setters
}