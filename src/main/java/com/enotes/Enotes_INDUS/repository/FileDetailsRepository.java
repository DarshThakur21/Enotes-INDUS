package com.enotes.Enotes_INDUS.repository;

import com.enotes.Enotes_INDUS.model.FileDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileDetailsRepository extends JpaRepository<FileDetails,Integer> {
}
