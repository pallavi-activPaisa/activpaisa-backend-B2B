package com.activpaisa.loan_app.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String email;     // or phone
    private String phone;
    private String password;
}