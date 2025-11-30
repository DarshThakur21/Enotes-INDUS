package com.enotes.Enotes_INDUS.controller;

import com.enotes.Enotes_INDUS.dto.UserResponseDto;
import com.enotes.Enotes_INDUS.model.User;
import com.enotes.Enotes_INDUS.utils.CommonUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/manage")
public class UserController {


    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/user-profiles")
    public ResponseEntity<?> getAllUserProfiles(){
        User loggedInUser= CommonUtil.getLoggedInUser();
        UserResponseDto userResponseDto=modelMapper.map(loggedInUser, UserResponseDto.class);
        return CommonUtil.createBuildResponse(userResponseDto, HttpStatus.OK);

    }
}
