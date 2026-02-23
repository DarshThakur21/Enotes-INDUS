package com.enotes.Enotes_INDUS.service;

import com.enotes.Enotes_INDUS.model.RefreshToken;
import com.enotes.Enotes_INDUS.model.User;
import com.enotes.Enotes_INDUS.repository.RefreshTokenRepo;
import com.enotes.Enotes_INDUS.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl {
    private static final long REFRESH_TOKEN_EXPIRY_MS = 7L * 24 * 60 * 60 * 1000;

    @Autowired
    private RefreshTokenRepo refreshTokenRepo;

    @Autowired
    private UserRepo userRepo;

    public RefreshToken createRefreshToken(Integer userId){
        User user=userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        refreshTokenRepo.findByUserId(userId).ifPresent(refreshTokenRepo :: delete);

        RefreshToken token=new RefreshToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(Instant.now().plusMillis(REFRESH_TOKEN_EXPIRY_MS));
        return refreshTokenRepo.save(token);
    }

    public Optional<RefreshToken> findToken(String token){
        return refreshTokenRepo.findByToken(token);
    }

}
