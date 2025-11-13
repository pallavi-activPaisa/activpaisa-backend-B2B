package com.activpaisa.loan_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.activpaisa.loan_app.entities.Organization;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, String> {
}
