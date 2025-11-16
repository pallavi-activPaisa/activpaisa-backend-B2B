package com.activpaisa.loan_app.services;

import com.activpaisa.loan_app.dtos.SignupRequest;
import com.activpaisa.loan_app.dtos.SignupResponse;
import com.activpaisa.loan_app.entities.Department;
import com.activpaisa.loan_app.entities.Designation;
import com.activpaisa.loan_app.entities.EmployeeProfile;
import com.activpaisa.loan_app.entities.Organization;
import com.activpaisa.loan_app.entities.PartnerProfile;
import com.activpaisa.loan_app.entities.Portal;
import com.activpaisa.loan_app.entities.User;
import com.activpaisa.loan_app.repositories.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class SignupService {

    private final OrganizationRepository organizationRepository;
    private final PartnerProfileRepository partnerProfileRepository;
    private final UserRepository userRepository;
    private final EmployeeProfileRepository employeeProfileRepository;
    private final PortalRepository portalRepository;
    private final UserTypeRepository userTypeRepository;
    private final PartnerTypeRepository partnerTypeRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public SignupService(OrganizationRepository organizationRepository,
            PartnerProfileRepository partnerProfileRepository,
            UserRepository userRepository,
            EmployeeProfileRepository employeeProfileRepository,
            PortalRepository portalRepository,
            UserTypeRepository userTypeRepository,
            PartnerTypeRepository partnerTypeRepository) {
        this.organizationRepository = organizationRepository;
        this.partnerProfileRepository = partnerProfileRepository;
        this.userRepository = userRepository;
        this.employeeProfileRepository = employeeProfileRepository;
        this.portalRepository = portalRepository;
        this.userTypeRepository = userTypeRepository;
        this.partnerTypeRepository = partnerTypeRepository;
    }

    @Transactional
    public SignupResponse signup(SignupRequest req) {
        // basic validation
        if ((req.getEmail() == null || req.getEmail().isBlank()) &&
                (req.getPhone() == null || req.getPhone().isBlank())) {
            throw new IllegalArgumentException("Either email or phone is required.");
        }
        if (req.getPassword() == null || req.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password required (min 6 chars).");
        }
        // check duplicates
        if (req.getEmail() != null && userRepository.findAll().stream()
                .anyMatch(u -> req.getEmail().equalsIgnoreCase(u.getEmail()))) {
            throw new IllegalArgumentException("Email already in use.");
        }
        if (req.getPhone() != null && userRepository.findAll().stream()
                .anyMatch(u -> req.getPhone().equalsIgnoreCase(u.getPhone()))) {
            throw new IllegalArgumentException("Phone already in use.");
        }

        String normalizedUserType = req.getUserTypeId() == null ? "CUSTOMER" : req.getUserTypeId().toUpperCase();
        // auto-detect portal if not provided
        String portalId = req.getPortalId();
        if (portalId == null || portalId.isBlank()) {
            portalId = detectPortalForUserType(normalizedUserType);
        }

        // now branch by userType
        switch (normalizedUserType) {
            case "PARTNER":
                return signupPartner(req, portalId);
            case "EMPLOYEE":
                return signupEmployee(req, portalId);
            case "ADMIN":
                return signupAdmin(req, portalId);
            case "CUSTOMER":
            default:
                return signupCustomer(req, portalId);
        }
    }

    private String detectPortalForUserType(String userType) {
        // Basic mapping - ensure these portal entries exist in DB
        switch (userType) {
            case "ADMIN":
            case "EMPLOYEE":
                return findPortalIdByName("Admin");
            case "PARTNER":
                return findPortalIdByName("Partner");
            case "CUSTOMER":
            default:
                return findPortalIdByName("Customer");
        }
    }

    private String findPortalIdByName(String portalName) {
        Optional<Portal> p = portalRepository.findAll().stream()
                .filter(portal -> portalName.equalsIgnoreCase(portal.getName()))
                .findFirst();
        return p.map(Portal::getId).orElse(null);
    }

    private SignupResponse signupPartner(SignupRequest req, String portalId) {
        // create organization
        Organization org = new Organization();
        String orgId = UUID.randomUUID().toString();
        org.setId(orgId);
        org.setName(req.getOrgName() == null ? req.getLegalName() : req.getOrgName());
        org.setOrgType("PARTNER");
        org.setEntityType(req.getEntityType());
        if (req.getPartnerTypeId() != null) {
            // ensure partner type exists (optional)
            partnerTypeRepository.findById(req.getPartnerTypeId()).ifPresent(org::setPartnerType);
        }
        org.setStatus("ACTIVE");
        org.setCreatedAt(LocalDateTime.now());
        org.setUpdatedAt(LocalDateTime.now());
        // parent = ACTIVPAISA root (try find it)
        organizationRepository.findAll().stream()
                .filter(o -> "ACTIVPAISA".equalsIgnoreCase(o.getCode()) || "ACTIVPAISA".equalsIgnoreCase(o.getName()))
                .findFirst()
                .ifPresent(o -> org.setParentOrg(o));
        organizationRepository.save(org);

        // create partner profile
        PartnerProfile pp = new PartnerProfile();
        pp.setId(UUID.randomUUID().toString());
        pp.setOrganization(org);
        pp.setLegalName(req.getLegalName());
        pp.setTradeName(req.getTradeName());
        pp.setGstNumber(req.getGstNumber());
        pp.setPanNumber(req.getPanNumber());
        pp.setWebsite(req.getWebsite());
        pp.setVerifiedStatus("PENDING");
        pp.setCreatedAt(LocalDateTime.now());
        pp.setUpdatedAt(LocalDateTime.now());
        partnerProfileRepository.save(pp);

        // create user (primary contact / partner admin)
        User user = new User();
        String userId = UUID.randomUUID().toString();
        user.setId(userId);
        user.setOrganization(org);
        // assign userType if available
        userTypeRepository.findAll().stream()
                .filter(ut -> "PARTNER".equalsIgnoreCase(ut.getName())
                        || "PARTNER_ADMIN".equalsIgnoreCase(ut.getName()))
                .findFirst()
                .ifPresent(user::setUserType);
        if (portalId != null) {
            portalRepository.findById(portalId).ifPresent(user::setPortal);
        }
        user.setEmail(req.getEmail());
        user.setPhone(req.getPhone());
        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        user.setStatus("ACTIVE");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        if (req.getCreatedByUserId() != null) {
            userRepository.findById(req.getCreatedByUserId()).ifPresent(user::setCreatedBy);
        }
        userRepository.save(user);

        return new SignupResponse(userId, orgId, "Partner signup successful. Pending verification if required.");
    }

    private SignupResponse signupEmployee(SignupRequest req, String portalId) {
        // EMPLOYEE must belong to an existing org
        if (req.getOrgId() == null) {
            throw new IllegalArgumentException("orgId is required for employee signup.");
        }
        Organization org = organizationRepository.findById(req.getOrgId())
                .orElseThrow(() -> new IllegalArgumentException("Organization not found: " + req.getOrgId()));

        // create user
        User user = new User();
        String userId = UUID.randomUUID().toString();
        user.setId(userId);
        user.setOrganization(org);
        userTypeRepository.findAll().stream()
                .filter(ut -> "EMPLOYEE".equalsIgnoreCase(ut.getName()))
                .findFirst()
                .ifPresent(user::setUserType);
        if (portalId != null) {
            portalRepository.findById(portalId).ifPresent(user::setPortal);
        }
        user.setEmail(req.getEmail());
        user.setPhone(req.getPhone());
        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        user.setStatus("ACTIVE");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        if (req.getCreatedByUserId() != null) {
            userRepository.findById(req.getCreatedByUserId()).ifPresent(user::setCreatedBy);
        }
        userRepository.save(user);

        // create employee profile
        EmployeeProfile ep = new EmployeeProfile();
        ep.setId(UUID.randomUUID().toString());
        ep.setUser(user);
        ep.setOrganization(org);
        ep.setDepartment(req.getDepartmentId() != null ? new Department() {
            {
                setId(req.getDepartmentId());
            }
        } : null);
        ep.setDesignation(req.getDesignationId() != null ? new Designation() {
            {
                setId(req.getDesignationId());
            }
        } : null);
        ep.setTeam(req.getOrgId() != null ? null : null); // keep null unless provided
        ep.setReportingManager(req.getReportingManagerId() != null ? new User() {
            {
                setId(req.getReportingManagerId());
            }
        } : null);
        ep.setJoinDate(null);
        ep.setStatus("ACTIVE");
        employeeProfileRepository.save(ep);

        return new SignupResponse(userId, org.getId(), "Employee signup successful.");
    }

    private SignupResponse signupCustomer(SignupRequest req, String portalId) {
        // Customers: create only user (and optionally a customer record later). For
        // simplicity we create user only.
        User user = new User();
        String userId = UUID.randomUUID().toString();
        user.setId(userId);
        userTypeRepository.findAll().stream()
                .filter(ut -> "CUSTOMER".equalsIgnoreCase(ut.getName()))
                .findFirst()
                .ifPresent(user::setUserType);
        if (portalId != null) {
            portalRepository.findById(portalId).ifPresent(user::setPortal);
        }
        user.setEmail(req.getEmail());
        user.setPhone(req.getPhone());
        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        user.setStatus("ACTIVE");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        if (req.getCreatedByUserId() != null) {
            userRepository.findById(req.getCreatedByUserId()).ifPresent(user::setCreatedBy);
        }
        userRepository.save(user);

        return new SignupResponse(userId, null, "Customer signup successful.");
    }

    private SignupResponse signupAdmin(SignupRequest req, String portalId) {
        // Admins belong to ACTIVPAISA org (root) - try to find it
        Organization root = organizationRepository.findAll().stream()
                .filter(o -> "ACTIVPAISA".equalsIgnoreCase(o.getCode()) || "ACTIVPAISA".equalsIgnoreCase(o.getName()))
                .findFirst()
                .orElse(null);

        User user = new User();
        String userId = UUID.randomUUID().toString();
        user.setId(userId);
        if (root != null)
            user.setOrganization(root);
        userTypeRepository.findAll().stream()
                .filter(ut -> "ADMIN".equalsIgnoreCase(ut.getName()) || "SUPER ADMIN".equalsIgnoreCase(ut.getName()))
                .findFirst()
                .ifPresent(user::setUserType);
        if (portalId != null) {
            portalRepository.findById(portalId).ifPresent(user::setPortal);
        }
        user.setEmail(req.getEmail());
        user.setPhone(req.getPhone());
        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        user.setStatus("ACTIVE");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        if (req.getCreatedByUserId() != null) {
            userRepository.findById(req.getCreatedByUserId()).ifPresent(user::setCreatedBy);
        }
        userRepository.save(user);

        // optional employee profile under root
        if (root != null) {
            EmployeeProfile ep = new EmployeeProfile();
            ep.setId(UUID.randomUUID().toString());
            ep.setUser(user);
            ep.setOrganization(root);
            ep.setStatus("ACTIVE");
            employeeProfileRepository.save(ep);
        }

        return new SignupResponse(userId, root != null ? root.getId() : null, "Admin signup successful.");
    }
}