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
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {


    @Autowired
    private CategoryRepository categoryRepository;


    @Autowired
    private ModelMapper mapper;


    @Override
    public CategoryDto getCategoryById(Integer id) {
        Optional<Category> categoryById = categoryRepository.findByIdAndIsDeletedFalse(id);

        if(categoryById.isPresent()){
            Category category= categoryById.get();
            return mapper.map(category,CategoryDto.class);


        }

        return  null;
    }

    @Override
    public Boolean saveCategory(CategoryDto categoryDto) {

//        Category category=new Category();
//
//        category.setName(categoryDto.getName());
//        category.setIsActive(categoryDto.getIsActive());
          Category category= mapper.map(categoryDto,Category.class);

          if(ObjectUtils.isEmpty(category.getId())){
              category.setIsDeleted(false);
              category.setCreatedBy(4);
              category.setCreatedOn(new Date());
          }
          else{
              updateCategory(category);
          }



        Category savedCategory =   categoryRepository.save(category);
                return  !ObjectUtils.isEmpty(savedCategory);

    }

    private void updateCategory(Category category) {
        Optional<Category> findCategory =categoryRepository.findById(category.getId());
        if(findCategory.isPresent()){
            Category existingCategory=findCategory.get();

            category.setIsDeleted(existingCategory.getIsDeleted());
            category.setCreatedBy(existingCategory.getCreatedBy());
            category.setCreatedOn(existingCategory.getCreatedOn());
            category.setUpdatedBy(1);
            category.setUpdatedOn(new Date());

        }

    }


    @Override
    public List<CategoryDto> getAllCategory() {
            List<Category> allCategory= categoryRepository.findAll();
           List<CategoryDto> categoryDtoList= allCategory.stream()
                   .filter(category -> !Boolean.TRUE.equals(category.getIsDeleted()))
                   .map(category -> mapper.map(category,CategoryDto.class)).toList();
        return categoryDtoList;

    }

    @Override
    public List<CategoryResponseDto> getActiveCategory() {
        List<Category> allCategory= categoryRepository.findByIsActiveTrueAndIsDeletedFalse();

        List<CategoryResponseDto> categoryResponseDtoList = allCategory.stream().map(category -> mapper.map(category,CategoryResponseDto.class)).toList();

        return categoryResponseDtoList;
    }

    @Override
    public Boolean deleteCategoryById(Integer id) {
        Optional<Category> categoryById =categoryRepository.findById(id);
        if (categoryById.isPresent()){
            Category category=categoryById.get();
            category.setIsDeleted(true);
            categoryRepository.save(category);
            mapper.map(category,CategoryDto.class);
            return true;
        }
        return false;
    }
}
