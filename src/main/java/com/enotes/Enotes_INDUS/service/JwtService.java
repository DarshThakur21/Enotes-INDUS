package com.enotes.Enotes_INDUS.service;

import com.enotes.Enotes_INDUS.model.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String generateToken(User user);

    String extractUsername(String token);

    Boolean validateToken(String token, UserDetails userDetails);
}
