package com.activpaisa.loan_app.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "partner_profiles")
@Getter
@Setter
public class PartnerProfile {

    @Id
    @Column(name = "partner_profile_id", length = 50)
    private String id;

    @ManyToOne
    @JoinColumn(name = "org_id")
    private Organization organization;

    @Column(name = "legal_name")
    private String legalName;

    @Column(name = "trade_name")
    private String tradeName;

    @Column(name = "gst_number")
    private String gstNumber;

    @Column(name = "cin_number")
    private String cinNumber;

    @Column(name = "pan_number")
    private String panNumber;

    @Column
    private String website;

    @Column(name = "verified_status")
    private String verifiedStatus;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Getters & Setters
}