package com.enotes.Enotes_INDUS.utils;

import com.enotes.Enotes_INDUS.dto.CategoryDto;
import com.enotes.Enotes_INDUS.dto.NotesDto;
import com.enotes.Enotes_INDUS.dto.TodoDto;
import com.enotes.Enotes_INDUS.dto.UserDto;
import com.enotes.Enotes_INDUS.exceptions.ValidationException;
import com.enotes.Enotes_INDUS.model.Role;
import com.enotes.Enotes_INDUS.repository.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;

@Component
public class Validations {
    private static final String EMAIL_REGEX="^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final String MOB_REGEX="^(\\+91|0)?[6-9][0-9]{9}$";


    @Autowired
    private RoleRepo roleRepo;


    public void categoryValidation(CategoryDto categoryDto) {

        Map<String, Object> error = new HashMap<>();
        if (ObjectUtils.isEmpty(categoryDto)) {
            throw new IllegalArgumentException("Category object/json not present it shouldnt be null or empty");
        } else {
            if (ObjectUtils.isEmpty(categoryDto.getName())) {
                error.put("Name:", "The Title shouldn't be empty");
            }


            if (ObjectUtils.isEmpty(categoryDto.getIsActive())) {
                error.put("isActive:", "Is invalid status of the category aaaaaaaaaaaaa");
            } else {
                if (categoryDto.getIsActive() != Boolean.TRUE.booleanValue() && categoryDto.getIsActive() != Boolean.FALSE.booleanValue()) {

                    error.put("isActive:", "Is invalid status of the category !");
                }

            }
        }
        if (!error.isEmpty()){
            throw new ValidationException(error);
        }

    }

    public void notesValidation(NotesDto notesDto){

        Map<String, Object> error = new HashMap<>();
        if(ObjectUtils.isEmpty(notesDto)){
            throw new IllegalArgumentException("Notes object/json not present it shouldnt be null or empty");

        }else{
            if(ObjectUtils.isEmpty(notesDto.getTitle())){
             error.put("Title","Title is not present");
            }
            if(ObjectUtils.isEmpty(notesDto.getDescription())){
                error.put("Description","Description is not present");

            }

//            if (!ObjectUtils.isEmpty(notesDto.getCategory())){
//                categoryValidation(notesDto.getCategory());
//            }


        }
        if (!error.isEmpty()){
            throw new ValidationException(error);
        }
    }

    public void todoValidation(TodoDto todoDto){
        Map<String, Object> error = new HashMap<>();

        if (ObjectUtils.isEmpty(todoDto)) {
            throw new IllegalArgumentException("todo  object/json not present it shouldnt be null or empty");
        } else {
            if (ObjectUtils.isEmpty(todoDto.getTitle())) {
                error.put("Title:", "The Title shouldn't be empty");
            }


            if (ObjectUtils.isEmpty(todoDto.getDescription())){
                error.put("Description:", "No description given");
            } else {
                if (ObjectUtils.isEmpty(todoDto.getStatus())){

                error.put("Status :", "give the appropriate status");
                }

            }
        }
        if (!error.isEmpty()){
            throw new ValidationException(error);
        }

    }



    public void userValidation(UserDto userDto){
        Map<String, Object> error = new HashMap<>();

        if(ObjectUtils.isEmpty(userDto)){
            throw new IllegalArgumentException("User  object/json not present it shouldnt be null or empty");
        }else{

            if(!StringUtils.hasText(userDto.getFirstName())){
                error.put("FirstName","The FirstName is empty field");


            }
            if(!StringUtils.hasText(userDto.getLastName())){
                error.put("LastName","The LastName is empty field");
            }

            if(!StringUtils.hasText(userDto.getEmail())  ||  !(userDto.getEmail().matches(EMAIL_REGEX))){
                error.put("Email","The Email is empty field or not a correct email address");
            }

            if(!StringUtils.hasText(userDto.getPassword())){
                error.put("Password","The Password is empty field");
            }
            if(!StringUtils.hasText(userDto.getMobileNo()) || !(userDto.getMobileNo().matches(MOB_REGEX))){
                error.put("MobileNo","The MobileNo is empty field or not a valid phone number ");
            }


           if(CollectionUtils.isEmpty(userDto.getRole())){
                error.put("Role","The Role is empty field or not a valid Role invalid ");

           }else{
               List<Integer> rolesIds=roleRepo.findAll().stream()
                       .map(r->r.getId())
                       .toList();

               List<Integer> invalidReqRoleIds=userDto.getRole().stream().map((role -> role.getId()))
                       .filter(roleId->!rolesIds.contains(roleId)).toList() ;
               if(CollectionUtils.isEmpty(invalidReqRoleIds)){
                   error.put("Role","The Role is empty field or not a valid Role invalid ");

               }

           }



        }
        if (!error.isEmpty()){
            throw new IllegalArgumentException(String.valueOf(error));
        }

    }






}

