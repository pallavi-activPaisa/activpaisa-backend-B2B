package com.activpaisa.loan_app.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponse {
    private String userId;
    private String orgId;
    private String userType;
    private String portal;
    private String token;   // JWT token
    private String message;
}