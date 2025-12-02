package com.enotes.Enotes_INDUS.controller;

import com.enotes.Enotes_INDUS.dto.PasswordResetRequestDto;
import com.enotes.Enotes_INDUS.exceptions.RegisterException;
import com.enotes.Enotes_INDUS.exceptions.ResourceNotFound;
import com.enotes.Enotes_INDUS.service.HomeService;
import com.enotes.Enotes_INDUS.service.UserService;
import com.enotes.Enotes_INDUS.utils.CommonUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.compress.PasswordRequiredException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/v1/home")
public class HomeController {

    @Autowired
    private HomeService homeService;

    @Autowired
    private UserService userService;

    @GetMapping("/str")
    public String hello(){
        return "hello this is my front page";
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestParam Integer uid,@RequestParam String code) throws RegisterException {
        Boolean status=homeService.verifyUser(uid,code);
        if(status){
            return CommonUtil.createBuildResponseMessage("Registered Successfully", HttpStatus.CREATED);
        }
            return CommonUtil.createBuildResponseMessage("Invalid Link ", HttpStatus.BAD_REQUEST);
    }


    @GetMapping("/reset-password-mail")
    public ResponseEntity<?> sendEmailForPasswordReset(@RequestParam String email, HttpServletRequest request) throws MessagingException, ResourceNotFound, UnsupportedEncodingException {
        userService.sendEmailPasswordReset(email,request);
        return CommonUtil.createBuildResponseMessage("Email sent for reset",HttpStatus.OK);
    }

    @GetMapping("/email-verify")
    public ResponseEntity<?> verifyPasswordResetLink(@RequestParam Integer uid,@RequestParam String resetCode) throws PasswordRequiredException, RegisterException, ResourceNotFound {
          userService.verifyReset(uid,resetCode);

            return CommonUtil.createBuildResponseMessage("Password reset Successfully verified", HttpStatus.OK);




    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPasssword(@RequestBody PasswordResetRequestDto passwordResetRequestDto){
            userService.resetPassword(passwordResetRequestDto);

        return CommonUtil.createBuildResponseMessage("PAssword reset successfully and functional",HttpStatus.OK);
    }



}
