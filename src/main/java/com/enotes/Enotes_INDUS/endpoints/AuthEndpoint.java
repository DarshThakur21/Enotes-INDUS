package com.enotes.Enotes_INDUS.endpoints;

import com.enotes.Enotes_INDUS.dto.LoginDto;
import com.enotes.Enotes_INDUS.dto.RefreshTokenRequest;
import com.enotes.Enotes_INDUS.dto.RefreshTokenResponse;
import com.enotes.Enotes_INDUS.dto.UserDto;
import com.enotes.Enotes_INDUS.exceptions.RegisterException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Tag(name = "Authentication" , description = "This is for the authentication" )
@RequestMapping("/api/v1/auth")
public interface AuthEndpoint {

    @Operation(tags = {"User Authentication"}, summary = "Register the User")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto, HttpServletRequest httpRequest) throws RegisterException;



    @Operation(tags = {"User Authentication"}, summary = "Login the User")
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDto loginDto);

    @Operation(tags = {"User Authentication"}, summary = "refresh the User refresh token")
    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken( @RequestBody RefreshTokenRequest request);

}
