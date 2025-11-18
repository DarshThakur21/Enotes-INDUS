package com.enotes.Enotes_INDUS.controller;


import com.enotes.Enotes_INDUS.dto.UserDto;
import com.enotes.Enotes_INDUS.service.UserService;
import com.enotes.Enotes_INDUS.utils.CommonUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/api/v1/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto, HttpServletRequest httpRequest){

        try {
String url=CommonUtil.getUrl(httpRequest);

        Boolean register=userService.registerUser(userDto,url);
        return CommonUtil.createBuildResponseMessage("Registered Success", HttpStatus.CREATED);
        }catch (Exception e){
            e.printStackTrace();
        return CommonUtil.createErrorResponseMessage("Cant register", HttpStatus.BAD_REQUEST);

        }

    }


    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserDto userDto){
        try {
            Boolean register=userService.loginUser(userDto);
            return CommonUtil.createBuildResponseMessage("login Success", HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return CommonUtil.createErrorResponseMessage("Cant login", HttpStatus.BAD_REQUEST);

        }

    }



}
