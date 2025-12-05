package com.enotes.Enotes_INDUS.controller;

import com.enotes.Enotes_INDUS.dto.PasswordChangeRequest;
import com.enotes.Enotes_INDUS.dto.UserResponseDto;
import com.enotes.Enotes_INDUS.model.User;
import com.enotes.Enotes_INDUS.service.UserService;
import com.enotes.Enotes_INDUS.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/user-profiles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUserProfiles(){
        log.info("UserController : getAllUserProfiles() : Start ");
        User loggedInUser= CommonUtil.getLoggedInUser();
        UserResponseDto userResponseDto=modelMapper.map(loggedInUser, UserResponseDto.class);
        log.info("UserController : getAllUserProfiles() : End ");
        return CommonUtil.createBuildResponse(userResponseDto, HttpStatus.OK);

    }


    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest passwordChangeRequest){
        log.info("UserController : changePassword() : Start ");
        if(userService.changePassword(passwordChangeRequest)){

            log.info("UserController : changePassword() : End");
            return CommonUtil.createBuildResponseMessage("Password Changed successfully", HttpStatus.OK);
        }
        log.info("UserController : changePassword() : End");
        return CommonUtil.createErrorResponseMessage("Password Couldnt be change please try again later", HttpStatus.FORBIDDEN);


    }

}
