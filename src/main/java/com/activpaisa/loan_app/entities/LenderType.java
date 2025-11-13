package com.activpaisa.loan_app.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "lender_types")
@Getter
@Setter
public class LenderType {

    @Id
    @Column(name = "lender_type_id", length = 50)
    private String id;

    @Column(name = "lender_type_name")
    private String name;

    @Column
    private String description;

    @OneToMany(mappedBy = "lenderType")
    private List<Lender> lenders;

    // Getters & Setters
}
