package com.enotes.Enotes_INDUS.controller;


import com.enotes.Enotes_INDUS.dto.LoginDto;
import com.enotes.Enotes_INDUS.dto.LoginResponse;
import com.enotes.Enotes_INDUS.dto.UserDto;
import com.enotes.Enotes_INDUS.service.AuthService;
import com.enotes.Enotes_INDUS.utils.CommonUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/api/v1/auth")
@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto, HttpServletRequest httpRequest){

        try {
String url=CommonUtil.getUrl(httpRequest);

        Boolean register= authService.registerUser(userDto,url);
        return CommonUtil.createBuildResponseMessage("Registered Success", HttpStatus.CREATED);
        }catch (Exception e){
            e.printStackTrace();
        return CommonUtil.createErrorResponseMessage("User already present", HttpStatus.BAD_REQUEST);

        }

    }


    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDto loginDto){

            LoginResponse loginResponse= authService.loginUser(loginDto);

            if(ObjectUtils.isEmpty(loginResponse)){
            return CommonUtil.createBuildResponseMessage("cant login", HttpStatus.BAD_REQUEST);
            }

            return CommonUtil.createBuildResponse(loginResponse, HttpStatus.OK);
    }




}
