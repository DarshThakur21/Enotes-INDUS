package com.enotes.Enotes_INDUS.service;

import com.enotes.Enotes_INDUS.dto.UserDto;

public interface UserService {

    Boolean registerUser(UserDto userDto);

    Boolean loginUser(UserDto userDto);

}
