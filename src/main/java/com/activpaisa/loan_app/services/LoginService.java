package com.activpaisa.loan_app.services;

import com.activpaisa.loan_app.dtos.LoginRequest;
import com.activpaisa.loan_app.dtos.LoginResponse;
import com.activpaisa.loan_app.entities.User;
import com.activpaisa.loan_app.repositories.UserRepository;
import com.activpaisa.loan_app.security.JwtUtil;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LoginService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public LoginService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public LoginResponse login(LoginRequest req) {
        User user = null;

        // login using email
        if (req.getEmail() != null) {
            user = userRepository.findAll().stream()
                    .filter(u -> req.getEmail().equalsIgnoreCase(u.getEmail()))
                    .findFirst()
                    .orElse(null);
        }

        // login using phone
        if (user == null && req.getPhone() != null) {
            user = userRepository.findAll().stream()
                    .filter(u -> req.getPhone().equalsIgnoreCase(u.getPhone()))
                    .findFirst()
                    .orElse(null);
        }

        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        // validate password
        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid password");
        }

        // generate JWT
        String token = jwtUtil.generateToken(user.getId(), user.getUserType().getName());

        // update last_login
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        return new LoginResponse(
                user.getId(),
                user.getOrganization() != null ? user.getOrganization().getId() : null,
                user.getUserType().getName(),
                user.getPortal() != null ? user.getPortal().getName() : null,
                token,
                "Login successful"
        );
    }
}