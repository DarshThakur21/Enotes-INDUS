package com.enotes.Enotes_INDUS.service;

import com.enotes.Enotes_INDUS.dto.*;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public interface AuthService {

    Boolean registerUser(UserDto userDto,String url) throws MessagingException, UnsupportedEncodingException;

    LoginResponse loginUser(LoginDto loginDto );

    RefreshTokenResponse refreshTokenResponse(RefreshTokenRequest request);

}
