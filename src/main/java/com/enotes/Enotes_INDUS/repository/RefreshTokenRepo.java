package com.enotes.Enotes_INDUS.repository;

import com.enotes.Enotes_INDUS.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepo extends JpaRepository<RefreshToken,Integer> {
    Optional<RefreshToken> findByUserId(Integer userId);

    Optional<RefreshToken> findByToken(String token);
}
