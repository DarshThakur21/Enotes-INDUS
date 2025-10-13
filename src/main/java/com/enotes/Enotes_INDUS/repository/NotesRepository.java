package com.enotes.Enotes_INDUS.repository;

import com.enotes.Enotes_INDUS.model.Notes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotesRepository extends JpaRepository<Notes,Integer> {
   Boolean existsByTitleAndCategoryId(String title, Integer categoryId);
}
