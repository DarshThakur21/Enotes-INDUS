package com.enotes.Enotes_INDUS.controller;


import com.enotes.Enotes_INDUS.dto.*;
import com.enotes.Enotes_INDUS.endpoints.AuthEndpoint;
import com.enotes.Enotes_INDUS.exceptions.RegisterException;
import com.enotes.Enotes_INDUS.service.AuthService;
import com.enotes.Enotes_INDUS.service.RefreshTokenServiceImpl;
import com.enotes.Enotes_INDUS.utils.CommonUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AuthController implements AuthEndpoint {

    @Autowired
    private AuthService authService;

    @Autowired
    private RefreshTokenServiceImpl refreshTokenService;


    private void buildCookie(HttpServletResponse response,String name,String value,int age){
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(age)
                .sameSite("Lax")        // Use "None" only if on different domains entirely
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    @Override
    public ResponseEntity<?> registerUser(UserDto userDto, HttpServletRequest httpRequest) throws RegisterException {
        log.info("AuthController : registerUser() : Start");
        try {
        String url=CommonUtil.getUrl(httpRequest);

        Boolean register= authService.registerUser(userDto,url);
        log.info("AuthController : registerUser() : End");
        return CommonUtil.createBuildResponseMessage("Registered Success", HttpStatus.CREATED);
        }catch (Exception e){
            log.error("USER ALREADY PRESENT EXCEPTION");
            e.printStackTrace();
        return CommonUtil.createErrorResponseMessage("User already present", HttpStatus.BAD_REQUEST);

        }

    }

    @Override
    public ResponseEntity<?> loginUser(LoginDto loginDto, HttpServletResponse response){
            LoginResponse loginResponse= authService.loginUser(loginDto);
            if(ObjectUtils.isEmpty(loginResponse)){
                return CommonUtil.createBuildResponseMessage("cant login", HttpStatus.BAD_REQUEST);
            }
            buildCookie(response,"access_token",loginResponse.getToken(),15 * 60);
            buildCookie(response,"refresh_token",loginResponse.getRefreshToken(), 7 * 24 * 3600);

            loginResponse.setToken(null);
            loginResponse.setRefreshToken(null);

            return CommonUtil.createBuildResponse(loginResponse, HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refresh_token".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if (refreshToken == null) {
            return CommonUtil.createBuildResponseMessage("No refresh token found", HttpStatus.UNAUTHORIZED);
        }

        RefreshTokenRequest refreshRequest = new RefreshTokenRequest();
        refreshRequest.setToken(refreshToken);

        RefreshTokenResponse refreshTokenResponse = authService.refreshTokenResponse(refreshRequest);

        if (ObjectUtils.isEmpty(refreshTokenResponse)) {
            return CommonUtil.createBuildResponseMessage("cant refresh token", HttpStatus.BAD_REQUEST);
        }

        buildCookie(response, "access_token", refreshTokenResponse.getAccessToken(), 15 * 60);

        return CommonUtil.createBuildResponse("Token refreshed successfully", HttpStatus.OK);
    }

}
