package com.enotes.Enotes_INDUS.utils;

import com.enotes.Enotes_INDUS.dto.CategoryDto;
import com.enotes.Enotes_INDUS.exceptions.ValidationException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class Validations {

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
}

