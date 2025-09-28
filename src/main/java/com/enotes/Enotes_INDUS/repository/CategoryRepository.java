package com.enotes.Enotes_INDUS.repository;

import com.enotes.Enotes_INDUS.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer> {


    List<Category> findByIsActiveTrueAndIsDeletedFalse();

    Optional<Category> findByIdAndIsDeletedFalse(Integer id);

    Boolean existsByName(String name);
//    CategoryDto getCategoryById(Integer id);
}
