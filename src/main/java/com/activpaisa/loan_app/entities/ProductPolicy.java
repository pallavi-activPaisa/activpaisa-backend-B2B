package com.activpaisa.loan_app.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "product_policies")
@Getter
@Setter
public class ProductPolicy {

    @Id
    @Column(name = "policy_id", length = 50)
    private String id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "policy_key")
    private String key;

    @Column(name = "policy_value")
    private String value;

    // Getters & Setters
}