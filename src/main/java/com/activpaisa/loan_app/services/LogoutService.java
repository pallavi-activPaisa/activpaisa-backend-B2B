package com.activpaisa.loan_app.services;

import com.activpaisa.loan_app.entities.BlacklistedToken;
import com.activpaisa.loan_app.repositories.BlacklistedTokenRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LogoutService {

    private final BlacklistedTokenRepository blacklistedTokenRepository;

   public void logout(String token, String userId) {

    // 1. validate token first
   

    // 2. check already blacklisted
    boolean exists = blacklistedTokenRepository.existsByToken(token);

    if (exists) {
        throw new IllegalArgumentException("Token already blacklisted");
    }

    // 3. save token into blacklist
    BlacklistedToken blacklistedToken = BlacklistedToken.builder()
            .token(token)
            .userId(String.valueOf(userId))
            .blacklistedAt(LocalDateTime.now())
            .build();

    blacklistedTokenRepository.save(blacklistedToken);
}

}
