package com.enotes.Enotes_INDUS.service.impl;

import com.enotes.Enotes_INDUS.dto.CategoryDto;
import com.enotes.Enotes_INDUS.dto.CategoryResponseDto;
import com.enotes.Enotes_INDUS.exceptions.ExistDataException;
import com.enotes.Enotes_INDUS.exceptions.GlobalExceptionsHandler;
import com.enotes.Enotes_INDUS.exceptions.ResourceNotFound;
import com.enotes.Enotes_INDUS.model.Category;
import com.enotes.Enotes_INDUS.repository.CategoryRepository;
import com.enotes.Enotes_INDUS.service.CategoryService;
import com.enotes.Enotes_INDUS.utils.Validations;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService  {


    @Autowired
    private CategoryRepository categoryRepository;


    @Autowired
    private ModelMapper mapper;


    @Autowired
    private Validations validations;



    @Override
    public CategoryDto getCategoryById(Integer id) throws ResourceNotFound {
        log.info("CategoryServiceImpl : getCategoryById() : Start ");
        Category categoryById = categoryRepository.findByIdAndIsDeletedFalse(id).orElseThrow(()->new ResourceNotFound("Category not found from implied exception "));

        if(ObjectUtils.isEmpty(categoryById)){
//            Category category= categoryById;
            log.info("category not present");
            return  null;
        }
        log.info("Category Found");
        log.info("CategoryServiceImpl : getCategoryById() : END");
        return mapper.map(categoryById,CategoryDto.class);

    }

    @Override
    public Boolean saveCategory(CategoryDto categoryDto) throws HttpMessageNotReadableException {
        log.info("CategoryServiceImpl : saveCategory() : Start ");

//        Category category=new Category();
//
//        category.setName(categoryDto.getName());
//        category.setIsActive(categoryDto.getIsActive());




        validations.categoryValidation(categoryDto);

          Boolean categoryExists= categoryRepository.existsByName(categoryDto.getName().trim());

          if(categoryExists){
              log.error("category already exists");
            throw new ExistDataException("Category already exist");
          }



          Category category= mapper.map(categoryDto,Category.class);





          if(ObjectUtils.isEmpty(category.getId())){
              category.setIsDeleted(false);

          }
          else{
              updateCategory(category);
              log.info("Category Updated");
          }


            Category savedCategory =   categoryRepository.save(category);
        log.info("Category Saved");
        log.info("CategoryServiceImpl : saveCategory() : END ");
          return  !ObjectUtils.isEmpty(savedCategory);

    }

    private void updateCategory(Category category) {
        Optional<Category> findCategory =categoryRepository.findById(category.getId());
        if(findCategory.isPresent()){
            Category existingCategory=findCategory.get();

            category.setIsDeleted(existingCategory.getIsDeleted());
            category.setCreatedBy(existingCategory.getCreatedBy());
            category.setCreatedOn(existingCategory.getCreatedOn());

        }

    }


    @Override
    public List<CategoryDto> getAllCategory() {
        log.info("CategoryServiceImpl : getAllCategory() : Start");

        List<Category> allCategory= categoryRepository.findAll();
        List<CategoryDto> categoryDtoList= allCategory.stream()
                   .filter(category -> !Boolean.TRUE.equals(category.getIsDeleted()))
                   .map(category -> mapper.map(category,CategoryDto.class)).toList();

        log.info("List found success");
        log.info("CategoryServiceImpl : getAllCategory() : END");
        return categoryDtoList;

    }

    @Override
    public List<CategoryResponseDto> getActiveCategory() {
        log.info("CategoryServiceImpl : getActiveCategory() : Start");

        List<Category> allCategory= categoryRepository.findByIsActiveTrueAndIsDeletedFalse();

        List<CategoryResponseDto> categoryResponseDtoList = allCategory.stream().map(category -> mapper.map(category,CategoryResponseDto.class)).toList();
        log.info("Active List Found");
        log.info("CategoryServiceImpl : getActiveCategory() : END");
        return categoryResponseDtoList;
    }

    @Override
    @CacheEvict(value = {"allCategory","getActiveCategory"},key = "#id")
    public Boolean deleteCategoryById(Integer id) {
        log.info("CategoryServiceImpl : deleteCategoryById() : Start");

        Optional<Category> categoryById =categoryRepository.findById(id);
        if (categoryById.isPresent()){
            Category category=categoryById.get();
            category.setIsDeleted(true);
            categoryRepository.save(category);
            mapper.map(category,CategoryDto.class);
            log.info("CategoryServiceImpl : Deleted Successfully");
            return true;
        }
        log.info("CategoryServiceImpl : deleteCategoryById() : END");
        log.info("CategoryServiceImpl : Cannot beb deleted");
        return false;
    }
}
