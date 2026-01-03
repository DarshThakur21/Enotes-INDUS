package com.enotes.Enotes_INDUS.repository;

import com.enotes.Enotes_INDUS.model.FavouriteNotes;
import com.enotes.Enotes_INDUS.model.Notes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FavouriteNotesRepository extends JpaRepository<FavouriteNotes,Integer> {

    @Query("""
       SELECT f\s
       FROM FavouriteNotes f\s
       JOIN f.notes n
       WHERE f.userId = :userId
         AND (n.isDeleted = false OR n.isDeleted IS NULL)
      \s""")
    List<FavouriteNotes> findByUserId(Integer userId);

    Boolean existsByUserIdAndNotesId(int userId, Integer notesId);
}
