package com.enotes.Enotes_INDUS.repository;

import com.enotes.Enotes_INDUS.model.FavouriteNotes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavouriteNotesRepository extends JpaRepository<FavouriteNotes,Integer> {
    List<FavouriteNotes> findByUserId(Integer userId);
}
