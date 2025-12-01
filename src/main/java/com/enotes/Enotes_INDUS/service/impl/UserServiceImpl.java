package com.enotes.Enotes_INDUS.service.impl;

import com.enotes.Enotes_INDUS.dto.PasswordChangeRequest;
import com.enotes.Enotes_INDUS.model.User;
import com.enotes.Enotes_INDUS.repository.UserRepo;
import com.enotes.Enotes_INDUS.service.UserService;
import com.enotes.Enotes_INDUS.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Boolean changePassword(PasswordChangeRequest passwordChangeRequest) {
        User loggedInUser = CommonUtil.getLoggedInUser();
Boolean passwordMatch=passwordEncoder.matches(passwordChangeRequest.getOldPassword(),loggedInUser.getPassword());
        if(!passwordMatch){
            return false;
//            throw new IllegalArgumentException("OLD PASSWORD DOES NOT MATCH");
        }
        loggedInUser.setPassword(passwordEncoder.encode(passwordChangeRequest.getNewPassword()));

        userRepo.save(loggedInUser);
        return  true;



    }
}
