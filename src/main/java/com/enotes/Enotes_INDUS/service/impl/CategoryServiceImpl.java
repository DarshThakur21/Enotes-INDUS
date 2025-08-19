package com.enotes.Enotes_INDUS.service.impl;

import com.enotes.Enotes_INDUS.dto.CategoryDto;
import com.enotes.Enotes_INDUS.dto.CategoryResponseDto;
import com.enotes.Enotes_INDUS.model.Category;
import com.enotes.Enotes_INDUS.repository.CategoryRepository;
import com.enotes.Enotes_INDUS.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {


    @Autowired
    private CategoryRepository categoryRepository;


    @Autowired
    private ModelMapper mapper;


    @Override
    public Boolean saveCategory(CategoryDto categoryDto) {

//        Category category=new Category();
//
//        category.setName(categoryDto.getName());
//        category.setIsActive(categoryDto.getIsActive());
          Category category= mapper.map(categoryDto,Category.class);


        category.setIsDeleted(false);
        category.setCreatedBy(4);
        category.setCreatedOn(new Date());
        Category savedCategory =   categoryRepository.save(category);
                return  !ObjectUtils.isEmpty(savedCategory);

    }


    @Override
    public List<CategoryDto> getAllCategory() {
            List<Category> allCategory= categoryRepository.findAll();
           List<CategoryDto> categoryDtoList= allCategory.stream().map(category -> mapper.map(category,CategoryDto.class)).toList();
        return categoryDtoList;

    }

    @Override
    public List<CategoryResponseDto> getActiveCategory() {
        List<Category> allCategory= categoryRepository.findByIsActiveTrue();

        List<CategoryResponseDto> categoryResponseDtoList = allCategory.stream().map(category -> mapper.map(category,CategoryResponseDto.class)).toList();

        return categoryResponseDtoList;
    }
}
