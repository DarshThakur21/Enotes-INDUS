package com.enotes.Enotes_INDUS.service.impl;

import com.enotes.Enotes_INDUS.exceptions.RegisterException;
import com.enotes.Enotes_INDUS.model.AccountStatus;
import com.enotes.Enotes_INDUS.model.User;
import com.enotes.Enotes_INDUS.repository.AccountStatuRepo;
import com.enotes.Enotes_INDUS.repository.UserRepo;
import com.enotes.Enotes_INDUS.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HomeServiceImpl implements HomeService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AccountStatuRepo accountStatuRepo;

    @Override
    public Boolean verifyUser(Integer uid, String code) throws RegisterException {
        Optional<User> userOptional=userRepo.findById(uid);
        User user=userOptional.get();
        AccountStatus status=user.getAccountStatus();
        if(status.getVerificationCode()==null){
            throw  new RegisterException("Account already registered");
        }


        if (!status.getVerificationCode().equals(code)) {
            return false;
        }

        status.setIsActive(true);
        status.setVerificationCode(null);
        accountStatuRepo.save(status);
    return  true;
    }




}
