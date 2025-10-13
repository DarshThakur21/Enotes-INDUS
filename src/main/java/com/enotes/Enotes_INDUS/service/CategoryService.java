package com.enotes.Enotes_INDUS.service;

import com.enotes.Enotes_INDUS.dto.CategoryDto;
import com.enotes.Enotes_INDUS.dto.CategoryResponseDto;
import com.enotes.Enotes_INDUS.exceptions.ResourceNotFound;
import com.enotes.Enotes_INDUS.model.Category;

import java.util.List;

public interface CategoryService {

    public Boolean saveCategory(CategoryDto  categoryDto );

   public  CategoryDto getCategoryById(Integer id) throws ResourceNotFound;


    public List<CategoryDto> getAllCategory();


    List<CategoryResponseDto> getActiveCategory();

    Boolean deleteCategoryById(Integer id);
}
