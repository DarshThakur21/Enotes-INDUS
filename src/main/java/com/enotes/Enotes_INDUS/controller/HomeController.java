package com.enotes.Enotes_INDUS.controller;

import com.enotes.Enotes_INDUS.dto.PasswordResetRequestDto;
import com.enotes.Enotes_INDUS.endpoints.HomeEndpoint;
import com.enotes.Enotes_INDUS.exceptions.RegisterException;
import com.enotes.Enotes_INDUS.exceptions.ResourceNotFound;
import com.enotes.Enotes_INDUS.service.HomeService;
import com.enotes.Enotes_INDUS.service.UserService;
import com.enotes.Enotes_INDUS.utils.CommonUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.PasswordRequiredException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
public class HomeController implements HomeEndpoint {

    Logger log= LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private HomeService homeService;

    @Autowired
    private UserService userService;

    @GetMapping("/str")
    public String hello(){

        log.info("HomeController : hello() : Execution Start ");
        return "hello this is my front page";
    }

    @Override
    public ResponseEntity<?> verifyUser(Integer uid, String code) throws RegisterException, ResourceNotFound {
        log.info("HomeController : verifyUser() : Execution Start ");
        Boolean status=homeService.verifyUser(uid,code);
        if(status){
            return CommonUtil.createBuildResponseMessage("Registered Successfully", HttpStatus.CREATED);
        }
        log.info("HomeController : verifyUser : Execution End ");
            return CommonUtil.createBuildResponseMessage("Invalid Link ", HttpStatus.BAD_REQUEST);
    }


    @Override
    public ResponseEntity<?> sendEmailForPasswordReset( String email, HttpServletRequest request) throws MessagingException, ResourceNotFound, UnsupportedEncodingException {
        log.info("HomeController : sendEmailForPasswordReset() : Start");
        userService.sendEmailPasswordReset(email,request);

        log.info("HomeController : sendEmailForPasswordReset() : End");
        return CommonUtil.createBuildResponseMessage("Email sent for reset",HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> verifyPasswordResetLink(Integer uid, String resetCode) throws PasswordRequiredException, RegisterException, ResourceNotFound {
        log.info("HomeController : verifyPasswordResetLink() : Start");
          userService.verifyReset(uid,resetCode);
        log.info("HomeController : verifyPasswordResetLink() : End");
        return CommonUtil.createBuildResponseMessage("Password reset Successfully verified", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> resetPasssword(@RequestBody PasswordResetRequestDto passwordResetRequestDto){
        log.info("HomeController : resetPasssword() : Start");
        userService.resetPassword(passwordResetRequestDto);
        log.info("HomeController : resetPasssword() : End");
        return CommonUtil.createBuildResponseMessage("Password reset successfully and functional",HttpStatus.OK);
    }
}
