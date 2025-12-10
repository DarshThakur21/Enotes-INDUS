package com.enotes.Enotes_INDUS.controller;

import com.enotes.Enotes_INDUS.dto.CategoryDto;
import com.enotes.Enotes_INDUS.dto.CategoryResponseDto;
import com.enotes.Enotes_INDUS.endpoints.CategoryEndpoint;
import com.enotes.Enotes_INDUS.exceptions.ResourceNotFound;
import com.enotes.Enotes_INDUS.model.Category;
import com.enotes.Enotes_INDUS.service.CategoryService;
import com.enotes.Enotes_INDUS.utils.CommonUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;


@Slf4j
@RestController
public class CategoryController implements CategoryEndpoint
{

    @Autowired
    private CategoryService categoryService;

    @Override
    public ResponseEntity<?> saveCategory(CategoryDto categoryDto){
        log.info("CategoryController : saveCategory() : Start ");
         Boolean savedSuccessCategory=categoryService.saveCategory(categoryDto);

         if(!savedSuccessCategory){

            log.info("Category Not Saved ");
            return CommonUtil.createErrorResponseMessage("NotSaved",HttpStatus.INTERNAL_SERVER_ERROR);
         }
            log.info("Category Saved Success ");
            return CommonUtil.createBuildResponseMessage("Saved Success",HttpStatus.CREATED);

    }

    @Override
    @Cacheable("allCategory")
    public ResponseEntity<?> getAllCategories(){
        log.info("CategoryController : getAllCategories() : Start ");
            List<CategoryDto> allCategories= categoryService.getAllCategory();

            if(CollectionUtils.isEmpty(allCategories)){
            log.info("Couldnt get categories ");
                return ResponseEntity.noContent().build();
            }

            log.info("Fetched successfully ");
            log.info("CategoryController : getAllCategories() : End ");
            return CommonUtil.createBuildResponse( allCategories, HttpStatus.OK);
    }

    @Override
    @Cacheable("getActiveCategory")
    public ResponseEntity<?> getActiveCategories(){
            log.info("CategoryController : getActiveCategories() : Start ");
        List<CategoryResponseDto> activeCategory= categoryService.getActiveCategory();

        if(CollectionUtils.isEmpty(activeCategory)){
            log.info("No Category find");
            return ResponseEntity.noContent().build();
        }
            log.info("Categories find");
            log.info("CategoryController : getActiveCategories() : END ");
        return CommonUtil.createBuildResponse(activeCategory,HttpStatus.OK);

    }

    @Override
    public ResponseEntity<?> getCategoryDetailsById(Integer id) throws Exception {
        log.info("CategoryController : getCategoryDetailsById() : Start ");
        CategoryDto categoryDto = categoryService.getCategoryById(id);

        if (ObjectUtils.isEmpty(categoryDto)) {
        log.info("No category with {} id  ",id);
            return  CommonUtil.createErrorResponseMessage("Internal server error" ,HttpStatus.OK);
        }

        log.info("Category with {}  ",id);
        log.info("CategoryController : getCategoryDetailsById() : END ");
        return CommonUtil.createBuildResponse(categoryDto, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<?> deleteCategoryById(Integer id){
        log.info("CategoryController : deleteCategoryById() : Start ");
        Boolean deleteCategory = categoryService.deleteCategoryById(id);


        if(!deleteCategory){
            log.info("Category cannot be deleted");
            return  CommonUtil.createErrorResponseMessage("Cannot delete category " ,HttpStatus.NOT_FOUND);
        }

        log.info("Category deleted Success");
        log.info("CategoryController : deleteCategoryById() : END");
        return CommonUtil.createBuildResponseMessage("Deleted category with id= "+id, HttpStatus.OK);

    }
}
