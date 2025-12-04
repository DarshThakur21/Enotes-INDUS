package com.enotes.Enotes_INDUS.service.impl;

import com.enotes.Enotes_INDUS.exceptions.RegisterException;
import com.enotes.Enotes_INDUS.exceptions.ResourceNotFound;
import com.enotes.Enotes_INDUS.model.AccountStatus;
import com.enotes.Enotes_INDUS.model.User;
import com.enotes.Enotes_INDUS.repository.AccountStatuRepo;
import com.enotes.Enotes_INDUS.repository.UserRepo;
import com.enotes.Enotes_INDUS.service.HomeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Optional;


@Slf4j
@Service
public class HomeServiceImpl implements HomeService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AccountStatuRepo accountStatuRepo;

    @Override
    public Boolean verifyUser(Integer uid, String code) throws RegisterException, ResourceNotFound {
        log.info("HomeServiceImpl : verifyUser() : start ");

        Optional<User> userOptional=userRepo.findById(uid);
        User user=userOptional.get();
        if(ObjectUtils.isEmpty(user)){

        log.error("HomeServiceImpl : verifyUser() ");
            throw new ResourceNotFound("User not found");
        }
        AccountStatus status=user.getAccountStatus();
        if(status.getVerificationCode()==null){

        log.info("Message : Already verified");
            throw  new RegisterException("Account already registered");
        }


        if (!status.getVerificationCode().equals(code)) {
            return false;
        }

        status.setIsActive(true);
        status.setVerificationCode(null);
        accountStatuRepo.save(status);
        log.info("HomeServiceImpl : verifyUser() : END");
    return  true;
    }




}
