package com.enotes.Enotes_INDUS.service;

import com.enotes.Enotes_INDUS.dto.CategoryDto;
import com.enotes.Enotes_INDUS.dto.CategoryResponseDto;
import com.enotes.Enotes_INDUS.model.Category;

import java.util.List;

public interface CategoryService {


   public  CategoryDto getCategoryById(Integer id);

    public Boolean saveCategory(CategoryDto  categoryDto );

    public List<CategoryDto> getAllCategory();


    List<CategoryResponseDto> getActiveCategory();

    Boolean deleteCategoryById(Integer id);
}
