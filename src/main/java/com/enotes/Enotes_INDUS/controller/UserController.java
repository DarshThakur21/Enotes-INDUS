package com.enotes.Enotes_INDUS.controller;

import com.enotes.Enotes_INDUS.dto.PasswordChangeRequest;
import com.enotes.Enotes_INDUS.dto.UserResponseDto;
import com.enotes.Enotes_INDUS.model.User;
import com.enotes.Enotes_INDUS.service.UserService;
import com.enotes.Enotes_INDUS.utils.CommonUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/user-profiles")
    public ResponseEntity<?> getAllUserProfiles(){
        User loggedInUser= CommonUtil.getLoggedInUser();
        UserResponseDto userResponseDto=modelMapper.map(loggedInUser, UserResponseDto.class);
        return CommonUtil.createBuildResponse(userResponseDto, HttpStatus.OK);

    }


    @PostMapping("/change-password")
    public ResponseEntity<?> chagnePassword(@RequestBody PasswordChangeRequest passwordChangeRequest){
//        User loggedInUser= CommonUtil.getLoggedInUser();

        if(userService.changePassword(passwordChangeRequest)){

        return CommonUtil.createBuildResponseMessage("Password Changed successfully", HttpStatus.OK);
        }

        return CommonUtil.createErrorResponseMessage("Password Couldnt be change please try again later", HttpStatus.FORBIDDEN);


    }

}
