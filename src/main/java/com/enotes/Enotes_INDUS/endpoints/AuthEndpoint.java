package com.enotes.Enotes_INDUS.endpoints;

import com.enotes.Enotes_INDUS.dto.LoginDto;
import com.enotes.Enotes_INDUS.dto.UserDto;
import com.enotes.Enotes_INDUS.exceptions.RegisterException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/auth")
public interface AuthEndpoint {
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto, HttpServletRequest httpRequest) throws RegisterException;

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDto loginDto);

}
