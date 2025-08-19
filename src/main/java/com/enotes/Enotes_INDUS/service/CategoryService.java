package com.enotes.Enotes_INDUS.service;

import com.enotes.Enotes_INDUS.model.Category;

import java.util.List;

public interface CategoryService {

    public Boolean saveCategory(Category category);

    public List<Category> getAllCategory();




}
