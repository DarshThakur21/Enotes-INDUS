package com.enotes.Enotes_INDUS.controller;

import com.enotes.Enotes_INDUS.dto.CategoryDto;
import com.enotes.Enotes_INDUS.dto.CategoryResponseDto;
import com.enotes.Enotes_INDUS.exceptions.ResourceNotFound;
import com.enotes.Enotes_INDUS.model.Category;
import com.enotes.Enotes_INDUS.service.CategoryService;
import com.enotes.Enotes_INDUS.utils.CommonUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/v1/category")
public class CategoryController
{

    @Autowired
    private CategoryService categoryService;




    @PostMapping("/save-category")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> saveCategory(@Valid @RequestBody CategoryDto categoryDto){
        log.info("CategoryController : saveCategory() : Start ");
         Boolean savedSuccessCategory=categoryService.saveCategory(categoryDto);

         if(!savedSuccessCategory){

            log.info("Category Not Saved ");
            return CommonUtil.createErrorResponseMessage("NotSaved",HttpStatus.INTERNAL_SERVER_ERROR);
         }
            log.info("Category Saved Success ");
            return CommonUtil.createBuildResponseMessage("Saved Success",HttpStatus.CREATED);

    }

    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
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

    @GetMapping("/active")
    @PreAuthorize("hasRole('USER')")
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





    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getCategoryDetailsById(@PathVariable Integer id) throws Exception {
//        try {
//
//
//            CategoryDto categoryDto = categoryService.getCategoryById(id);
//
//
//            if (ObjectUtils.isEmpty(categoryDto)) {
//                return new ResponseEntity<>("category not found with id= " + id, HttpStatus.NOT_FOUND);
//            }
//            return new ResponseEntity<>(categoryDto, HttpStatus.OK);
//
//
//        }
//        catch(ResourceNotFound e){
//            log.error("controller :: getCategoryDetailsById ::",e.getMessage());
//
//            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
//
//
//        } catch (Exception e) {
//            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
//        }


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


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteCategoryById(@PathVariable Integer id){
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
