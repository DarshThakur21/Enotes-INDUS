package com.enotes.Enotes_INDUS.controller;

import com.enotes.Enotes_INDUS.dto.CategoryDto;
import com.enotes.Enotes_INDUS.dto.CategoryResponseDto;
import com.enotes.Enotes_INDUS.model.Category;
import com.enotes.Enotes_INDUS.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;


@RestController
@RequestMapping("/api/v1/category")
public class CategoryController
{

    @Autowired
    private CategoryService categoryService;




    @PostMapping("/save-category")
    public ResponseEntity<?> saveCategory(@RequestBody CategoryDto categoryDto){
         Boolean savedSuccessCategory=categoryService.saveCategory(categoryDto);

         if(savedSuccessCategory){

            return  new ResponseEntity<>("your category is saved", HttpStatus.CREATED);
         }

            return  new ResponseEntity<>("your category is not saved", HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @GetMapping("/")
    public ResponseEntity<?> getAllCategories(){
            List<CategoryDto> allCategories= categoryService.getAllCategory();

            if(CollectionUtils.isEmpty(allCategories)){
                return ResponseEntity.noContent().build();
            }

            return new ResponseEntity<>( allCategories, HttpStatus.OK);
    }

    @GetMapping("/active")
    public ResponseEntity<?> getActiveCategories(){
        List<CategoryResponseDto> activeCategory= categoryService.getActiveCategory();

        if(CollectionUtils.isEmpty(activeCategory)){
            return ResponseEntity.noContent().build();
        }

        return new ResponseEntity<>( activeCategory, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryDetailsById(@PathVariable Integer id){
        CategoryDto categoryDto = categoryService.getCategoryById(id);


        if(ObjectUtils.isEmpty(categoryDto)){
            return new ResponseEntity<>("category not found with id= "+id,HttpStatus.NOT_FOUND);
        }
            return new ResponseEntity<>(categoryDto,HttpStatus.OK);


    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategoryById(@PathVariable Integer id){
        Boolean deleteCategory = categoryService.deleteCategoryById(id);


        if(deleteCategory){
            return new ResponseEntity<>("Deleted category with id= "+id, HttpStatus.OK);
        }
            return new ResponseEntity<>("category not found with id= "+id, HttpStatus.NOT_FOUND);


    }





}
