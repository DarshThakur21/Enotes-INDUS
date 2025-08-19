package com.enotes.Enotes_INDUS.controller;

import com.enotes.Enotes_INDUS.model.Category;
import com.enotes.Enotes_INDUS.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
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
    public ResponseEntity<?> saveCategory(@RequestBody Category category){
         Boolean savedSuccessCategory=categoryService.saveCategory(category);

         if(savedSuccessCategory){

            return  new ResponseEntity<>("your category is saved", HttpStatus.CREATED);
         }

            return  new ResponseEntity<>("your category is not saved", HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategories(){
            List<Category> allCategories= categoryService.getAllCategory();

            if(CollectionUtils.isEmpty(allCategories)){
                return ResponseEntity.noContent().build();
            }

            return new ResponseEntity<>("This is your categories "+ allCategories, HttpStatus.OK);
    }




}
