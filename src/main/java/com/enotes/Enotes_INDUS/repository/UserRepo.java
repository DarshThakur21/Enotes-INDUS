package com.enotes.Enotes_INDUS.repository;

import com.enotes.Enotes_INDUS.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Integer> {
}
