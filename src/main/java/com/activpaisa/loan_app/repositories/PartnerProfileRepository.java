package com.activpaisa.loan_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.activpaisa.loan_app.entities.PartnerProfile;

@Repository
public interface PartnerProfileRepository extends JpaRepository<PartnerProfile, String> {
}