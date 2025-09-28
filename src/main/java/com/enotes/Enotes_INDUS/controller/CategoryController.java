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
    public ResponseEntity<?> saveCategory(@Valid @RequestBody CategoryDto categoryDto){
         Boolean savedSuccessCategory=categoryService.saveCategory(categoryDto);

         if(savedSuccessCategory){

            return CommonUtil.createBuildResponseMessage("Saved Success",HttpStatus.CREATED);


//            return  new ResponseEntity<>("your category is saved", HttpStatus.CREATED);
         }
            return CommonUtil.createErrorResponseMessage("NotSaved",HttpStatus.INTERNAL_SERVER_ERROR);
//            return   new ResponseEntity<>("your category is not saved", HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @GetMapping("/")
    public ResponseEntity<?> getAllCategories(){
            List<CategoryDto> allCategories= categoryService.getAllCategory();

            if(CollectionUtils.isEmpty(allCategories)){
                return ResponseEntity.noContent().build();
            }

            return CommonUtil.createBuildResponse( allCategories, HttpStatus.OK);
    }







    @GetMapping("/active")
    public ResponseEntity<?> getActiveCategories(){
        List<CategoryResponseDto> activeCategory= categoryService.getActiveCategory();

        if(CollectionUtils.isEmpty(activeCategory)){
            return ResponseEntity.noContent().build();
        }
        return CommonUtil.createBuildResponse(activeCategory,HttpStatus.OK);
//        return new ResponseEntity<>( activeCategory, HttpStatus.OK);
    }





    @GetMapping("/{id}")
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



        CategoryDto categoryDto = categoryService.getCategoryById(id);


        if (ObjectUtils.isEmpty(categoryDto)) {
//            return new ResponseEntity<>("category not found with id= " + id, HttpStatus.NOT_FOUND);
            return  CommonUtil.createErrorResponseMessage("Internal server error" ,HttpStatus.OK);
        }
//        return new ResponseEntity<>(categoryDto, HttpStatus.OK);

        return CommonUtil.createBuildResponse(categoryDto, HttpStatus.OK);





    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategoryById(@PathVariable Integer id){
        Boolean deleteCategory = categoryService.deleteCategoryById(id);


        if(deleteCategory){
//            return new ResponseEntity<>("Deleted category with id= "+id, HttpStatus.OK);
            return CommonUtil.createBuildResponseMessage("Deleted category with id= "+id, HttpStatus.OK);

        }
//            return new ResponseEntity<>("category not found with id= "+id, HttpStatus.NOT_FOUND);

        return  CommonUtil.createErrorResponseMessage("Internal server error" ,HttpStatus.NOT_FOUND);



    }





}
