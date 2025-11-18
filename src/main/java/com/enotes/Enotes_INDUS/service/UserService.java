package com.enotes.Enotes_INDUS.service;

import com.enotes.Enotes_INDUS.dto.UserDto;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public interface UserService {

    Boolean registerUser(UserDto userDto,String url) throws MessagingException, UnsupportedEncodingException;

    Boolean loginUser(UserDto userDto);

}
