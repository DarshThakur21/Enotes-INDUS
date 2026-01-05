package com.enotes.Enotes_INDUS.service.impl;

import com.enotes.Enotes_INDUS.dto.*;
import com.enotes.Enotes_INDUS.exceptions.RegisterException;
import com.enotes.Enotes_INDUS.exceptions.ResourceNotFound;
import com.enotes.Enotes_INDUS.model.AccountStatus;
import com.enotes.Enotes_INDUS.model.User;
import com.enotes.Enotes_INDUS.repository.UserRepo;
import com.enotes.Enotes_INDUS.service.EmailService;
import com.enotes.Enotes_INDUS.service.UserService;
import com.enotes.Enotes_INDUS.utils.CommonUtil;
import io.jsonwebtoken.lang.Strings;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.PasswordRequiredException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Override
    public Boolean changePassword(PasswordChangeRequest passwordChangeRequest) {
        log.info("UserServiceImpl : changePassword() : Start ");
        User loggedInUser = CommonUtil.getLoggedInUser();
        Boolean passwordMatch = passwordEncoder.matches(passwordChangeRequest.getOldPassword(), loggedInUser.getPassword());
        if (!passwordMatch) {
            log.info("Cannot change password");
            return false;
        }
        loggedInUser.setPassword(passwordEncoder.encode(passwordChangeRequest.getNewPassword()));

        userRepo.save(loggedInUser);
        log.info("Password change success");
        log.info("UserServiceImpl : changePassword() : End");
        return true;
    }

    @Override
    public void sendEmailPasswordReset(String email, HttpServletRequest request) throws ResourceNotFound, MessagingException, UnsupportedEncodingException {
        log.info("UserServiceImpl : sendEmailPasswordReset() : Start");

        Optional<User> userOptional = userRepo.findByEmail(email);
        User user = userOptional.get();
        if (ObjectUtils.isEmpty(user)) {
            log.error("Invalid email found please found again");
            throw new ResourceNotFound("Email Id does not exist INVALID EMAIL");
        }

        String passwordResetToken = UUID.randomUUID().toString();
        user.getAccountStatus().setPasswordResetToken(passwordResetToken);
        User updateUser = userRepo.save(user);
        emailForPasswordReset(updateUser, request);

        log.info("UserServiceImpl : sendEmailPasswordReset() : End");
    }

    private void emailForPasswordReset(User user, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        String url = CommonUtil.getUrl(request);
        String msg =
                "Hi, <b>[[firstname]] [[lastname]]</b><br>" +
                        "Your account requested for password reset.<br><br>" +
                        "Click the link below to reset your password:<br>" +
                        "<p><a href='[[url]]'>Reset my password</a></p>" +
//                        "<a href='[[url]]'>Click Here!!!</a><br><br>" +
                        "Thank you!";

        String frontendUrl = "http://localhost:5173";
        String verifyUrl = UriComponentsBuilder
                .fromHttpUrl(frontendUrl + "/reset-password")
                .queryParam("uid", user.getId())
                .queryParam("resetCode", user.getAccountStatus().getPasswordResetToken())
                .toUriString();

//        String verifyUrl= UriComponentsBuilder.fromHttpUrl(url+"/api/v1/home/email-verify")
//                .queryParam("uid", user.getId())
//                .queryParam("resetCode", user.getAccountStatus().getPasswordResetToken())
//                .toUriString();

        msg = msg.replace("[[firstname]]", user.getFirstName());
        msg = msg.replace("[[lastname]]", user.getLastName());
        msg = msg.replace("[[url]]", verifyUrl);


        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setTo(user.getEmail());
        emailRequest.setTitle("RESET LINK FOR PASSWORD");
        emailRequest.setSubject("RESET");
        emailRequest.setMessage(msg);

        emailService.sendEmail(emailRequest);

    }


    @Override
    public void verifyReset(Integer uid, String resetCode) throws PasswordRequiredException, ResourceNotFound {
        log.info("UserServiceImpl : verifyReset() : Start");
        Optional<User> userOptional = userRepo.findById(uid);
        User user = userOptional.get();
        if (ObjectUtils.isEmpty(user)) {
            throw new ResourceNotFound("invalid user");
        }
        verifyCode(user, resetCode);
        log.info("UserServiceImpl : verifyReset() : End");
    }

    @Override
    public Boolean editUserDetail(EditUserDto userDto) {
        Optional<User> userOptional = userRepo.findById(userDto.getId());
        User user = userOptional.get();
        if (ObjectUtils.isEmpty(user)) {
            return false;
        }
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setMobileNo(userDto.getMobileNo());
        userRepo.save(user);
    return true;

    }


    private void verifyCode(User user, String resetCode) {
        String userCode= user.getAccountStatus().getPasswordResetToken();

        if(StringUtils.hasText(resetCode)){
            if(!StringUtils.hasText(userCode)){
                throw new IllegalArgumentException("LINK EXPIRED");
            }

            if(!userCode.equals(resetCode)){
            throw new IllegalArgumentException("INVALID LINK");

            }
        }else{
            throw new IllegalArgumentException("INVALID TOKEN");
        }



    }

    @Override
    public void resetPassword(PasswordResetRequestDto passwordResetRequestDto) {
        log.info("UserServiceImpl : resetPassword() : Start");
        Optional<User> userOptional=userRepo.findById(passwordResetRequestDto.getUid());
        User user=userOptional.get();
        if(user==null){
            log.info("Current user is null");
        }
     String newPassword=   passwordEncoder.encode(passwordResetRequestDto.getNewPassword());
     user.setPassword(newPassword);
     user.getAccountStatus().setPasswordResetToken(null);
     userRepo.save(user);
     log.info("UserServiceImpl : resetPassword() : End");
    }

}
