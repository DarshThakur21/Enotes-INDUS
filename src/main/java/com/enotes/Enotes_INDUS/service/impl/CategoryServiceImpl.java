package com.enotes.Enotes_INDUS.service.impl;

import com.enotes.Enotes_INDUS.model.Category;
import com.enotes.Enotes_INDUS.repository.CategoryRepository;
import com.enotes.Enotes_INDUS.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {


    @Autowired
    private CategoryRepository categoryRepository;



    @Override
    public Boolean saveCategory(Category category) {



        category.setIsDeleted(false);

                category.setCreatedBy(212);
                category.setCreatedOn(new Date());
//                category.setUpdatedBy(122);

        Category savedCategory =   categoryRepository.save(category);
//            if(ObjectUtils.isEmpty(savedCategory))

                return  !ObjectUtils.isEmpty(savedCategory);
    }

    @Override
    public List<Category> getAllCategory() {
            List<Category> allCategory=         categoryRepository.findAll();


        return allCategory;
    }
}
