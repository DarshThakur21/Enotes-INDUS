package com.enotes.Enotes_INDUS.repository;

import com.enotes.Enotes_INDUS.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Integer> {


//    @Query("SELECT u from User u where u.email=:email ")
    Optional<User> findByEmail(String email);
}
