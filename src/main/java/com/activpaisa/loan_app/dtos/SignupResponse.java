package com.activpaisa.loan_app.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupResponse {
    private String userId;
    private String orgId;
    private String message;

    public SignupResponse() {}

    public SignupResponse(String userId, String orgId, String message) {
        this.userId = userId;
        this.orgId = orgId;
        this.message = message;
    }
    
}
