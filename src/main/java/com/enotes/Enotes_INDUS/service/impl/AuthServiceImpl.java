package com.enotes.Enotes_INDUS.service.impl;

import com.enotes.Enotes_INDUS.config.security.CustomUserDetails;
import com.enotes.Enotes_INDUS.dto.*;
import com.enotes.Enotes_INDUS.model.AccountStatus;
import com.enotes.Enotes_INDUS.model.Role;
import com.enotes.Enotes_INDUS.model.User;
import com.enotes.Enotes_INDUS.repository.RoleRepo;
import com.enotes.Enotes_INDUS.repository.UserRepo;
import com.enotes.Enotes_INDUS.service.JwtService;
import com.enotes.Enotes_INDUS.service.AuthService;
import com.enotes.Enotes_INDUS.service.EmailService;
import com.enotes.Enotes_INDUS.utils.Validations;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {


    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper mapper;


    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private Validations validations;


    @Autowired
    private EmailService emailService;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;





    @Override
    public Boolean registerUser(UserDto userDto,String url) throws MessagingException, UnsupportedEncodingException {


        validations.userValidation(userDto);
        setRole(userDto);
        String pass=userDto.getPassword();
        AccountStatus status=new AccountStatus();
                status.setIsActive(false);
                status.setVerificationCode(UUID.randomUUID().toString());

        User user=mapper.map(userDto,User.class);
            user.setAccountStatus(status);
            user.setPassword(passwordEncoder.encode(pass));

        User saveUser=userRepo.save(user);
        if(!ObjectUtils.isEmpty(saveUser)){
//            sendEmail email
            emailSend(saveUser,url);


            return true;

        }

        return false;
    }

    private void emailSend(User saveUser,String url) throws MessagingException, UnsupportedEncodingException {

        String msg =
                "Hi, <b>[[firstname]] [[lastname]]</b><br>" +
                        "Your account has been registered successfully.<br><br>" +
                        "Click the link below to verify your account:<br>" +
                        "<a href='[[url]]'>Click Here!!!</a><br><br>" +
                        "Thank you!";
        String verifyUrl=UriComponentsBuilder.fromHttpUrl(url+"/api/v1/home/verify")
                .queryParam("uid", saveUser.getId())
                .queryParam("code", saveUser.getAccountStatus().getVerificationCode())
                .toUriString();

        msg=msg.replace("[[firstname]]",saveUser.getFirstName());
        msg=msg.replace("[[lastname]]",saveUser.getLastName());
//        msg=msg.replace("[[url]]","http://localhost:8080/enotes/api/v1/home/verify?uid="+saveUser.getId()+"&code="+saveUser.getAccountStatus().getVerificationCode());
        msg=msg.replace("[[url]]",verifyUrl);



        EmailRequest request=new EmailRequest();
        request.setTo(saveUser.getEmail());
        request.setTitle("Account Creation Confirmation");
        request.setSubject("Register");
        request.setMessage(msg);

        emailService.sendEmail(request);
    }

    private void setRole(UserDto userDto) {
        List<Role> roleList=userDto.getRole().stream().map(r->roleRepo.findByRole(r.getRole())
                .orElseThrow(() -> new RuntimeException("Invalid role id"))).toList();

        userDto.setRole(roleList);

    }


//    private String hashUserPassword(String password) {
//
//    }

    @Override
    public LoginResponse loginUser(LoginDto loginDto) {
        log.info("AuthServiceImpl : loginUser() : start ");
        Authentication authentication =manager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword()));
    
        if(authentication.isAuthenticated()){
            CustomUserDetails customUserDetails= (CustomUserDetails) authentication.getPrincipal();
            String token= jwtService.generateToken(customUserDetails.getUser());
            log.info("AuthServiceImpl : loginUser() : success");
            return LoginResponse.builder()
                    .userDto(mapper.map(customUserDetails.getUser(), UserResponseDto.class))
                    .token(token)
                    .build();
        }
        log.info("AuthServiceImpl : cannot be authenticated");
        return null;
    }
}
