package com.activpaisa.loan_app.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
    // basic
    private String email;
    private String phone;
    private String password;

    // identity / profile
    private String userTypeId; // e.g. "CUSTOMER", "PARTNER", "EMPLOYEE", "ADMIN"
    private String portalId; // optional, AUTO-detected if null
    private String orgId; // for EMPLOYEE (existing org) or ADMIN (ActivPaisa)
    private String orgName; // for PARTNER creation
    private String entityType; // COMPANY / INDIVIDUAL (for partner)
    private String partnerTypeId; // optional

    // partner profile fields (optional)
    private String legalName;
    private String tradeName;
    private String panNumber;
    private String gstNumber;
    private String website;

    // employee profile fields (optional)
    private String departmentId;
    private String designationId;
    private String reportingManagerId;

    // createdBy (optional) — in case an admin creates this user
    private String createdByUserId;

}