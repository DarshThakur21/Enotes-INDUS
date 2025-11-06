package com.enotes.Enotes_INDUS.repository;

import com.enotes.Enotes_INDUS.model.Notes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotesRepository extends JpaRepository<Notes,Integer> {
   Boolean existsByTitleAndCategoryId(String title, Integer categoryId);

   Page<Notes> findByCreatedByAndIsDeletedFalse(Integer userId, Pageable pageable);


   List<Notes> findByCreatedByAndIsDeletedTrue(Integer userId);
}
