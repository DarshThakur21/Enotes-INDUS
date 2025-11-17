package com.enotes.Enotes_INDUS.service.impl;

import com.enotes.Enotes_INDUS.dto.UserDto;
import com.enotes.Enotes_INDUS.model.Role;
import com.enotes.Enotes_INDUS.model.User;
import com.enotes.Enotes_INDUS.repository.RoleRepo;
import com.enotes.Enotes_INDUS.repository.UserRepo;
import com.enotes.Enotes_INDUS.service.UserService;
import com.enotes.Enotes_INDUS.utils.Validations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper mapper;


    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private Validations validations;


    @Override
    public Boolean registerUser(UserDto userDto) {


        validations.userValidation(userDto);
        setRole(userDto);
        User user=mapper.map(userDto,User.class);

        User saveUser=userRepo.save(user);
        if(!ObjectUtils.isEmpty(saveUser)){

            return true;

        }

        return false;
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
    public Boolean loginUser(UserDto userDto) {
        return null;
    }
}
