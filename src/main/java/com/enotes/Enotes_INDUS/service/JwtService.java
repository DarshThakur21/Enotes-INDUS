package com.enotes.Enotes_INDUS.service;

import com.enotes.Enotes_INDUS.model.User;

public interface JwtService {
    String generateToken(User user);
}
