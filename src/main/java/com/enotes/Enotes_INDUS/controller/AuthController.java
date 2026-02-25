package com.enotes.Enotes_INDUS.controller;


import com.enotes.Enotes_INDUS.dto.*;
import com.enotes.Enotes_INDUS.endpoints.AuthEndpoint;
import com.enotes.Enotes_INDUS.exceptions.RegisterException;
import com.enotes.Enotes_INDUS.service.AuthService;
import com.enotes.Enotes_INDUS.service.RefreshTokenServiceImpl;
import com.enotes.Enotes_INDUS.utils.CommonUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AuthController implements AuthEndpoint {

    @Autowired
    private AuthService authService;

    @Autowired
    private RefreshTokenServiceImpl refreshTokenService;

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
    public ResponseEntity<?> loginUser(LoginDto loginDto){
//        log.info("AuthController : loginUser() : Start");

            LoginResponse loginResponse= authService.loginUser(loginDto);

            if(ObjectUtils.isEmpty(loginResponse)){
//        log.info("AuthController : loginUser() : Bad Request");
            return CommonUtil.createBuildResponseMessage("cant login", HttpStatus.BAD_REQUEST);
            }

//        log.info("AuthController : loginUser() : END");
            return CommonUtil.createBuildResponse(loginResponse, HttpStatus.OK);
    }
    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        RefreshTokenResponse refreshTokenResponse=authService.refreshTokenResponse(request);
        if(ObjectUtils.isEmpty(refreshTokenResponse)){
            return (ResponseEntity<RefreshTokenResponse>) CommonUtil.createBuildResponseMessage("cant refersh token", HttpStatus.BAD_REQUEST);
        }

        return (ResponseEntity<RefreshTokenResponse>) CommonUtil.createBuildResponse(refreshTokenResponse,HttpStatus.OK);
    }

}
