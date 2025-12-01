package com.enotes.Enotes_INDUS.service;

import com.enotes.Enotes_INDUS.dto.LoginDto;
import com.enotes.Enotes_INDUS.dto.LoginResponse;
import com.enotes.Enotes_INDUS.dto.UserDto;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public interface AuthService {

    Boolean registerUser(UserDto userDto,String url) throws MessagingException, UnsupportedEncodingException;

    LoginResponse loginUser(LoginDto loginDto );

}
