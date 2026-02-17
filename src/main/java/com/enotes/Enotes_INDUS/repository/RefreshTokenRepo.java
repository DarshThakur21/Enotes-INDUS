package com.enotes.Enotes_INDUS.repository;

import com.enotes.Enotes_INDUS.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepo extends JpaRepository<RefreshToken,Integer> {
}
