package com.activpaisa.loan_app.controllers;

import com.activpaisa.loan_app.dtos.LoginRequest;
import com.activpaisa.loan_app.dtos.LoginResponse;
import com.activpaisa.loan_app.dtos.SignupRequest;
import com.activpaisa.loan_app.dtos.SignupResponse;
import com.activpaisa.loan_app.security.JwtUtil;
import com.activpaisa.loan_app.services.LoginService;
import com.activpaisa.loan_app.services.LogoutService;
import com.activpaisa.loan_app.services.SignupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    private final SignupService signupService;

    private final LoginService loginService;

    private final LogoutService logoutService;

    private final JwtUtil jwtUtil;

    public AuthController(
            SignupService signupService,
            LoginService loginService,
            LogoutService logoutService,
            JwtUtil jwtUtil) {
        this.signupService = signupService;
        this.loginService = loginService;
        this.logoutService = logoutService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest req) {
        try {
            SignupResponse resp = signupService.signup(req);
            return ResponseEntity.ok(resp);
        } catch (IllegalArgumentException ex) {
            System.out.println(ex);
            return ResponseEntity.badRequest().body("Request Failed");
        } catch (Exception ex) {
            System.out.println(ex);
            ex.printStackTrace();
            return ResponseEntity.status(500).body("Internal server error");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        try {
            LoginResponse resp = loginService.login(req);
            return ResponseEntity.ok(resp);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(500).body("Internal server error");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("No token provided");
        }

        String token = authHeader.substring(7);

        // ✔ extract userId as UUID string
        String userId = jwtUtil.extractUserId(token);

        logoutService.logout(token, userId);

        return ResponseEntity.ok("Logged out successfully");
    }

}