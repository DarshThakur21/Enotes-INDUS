package com.enotes.Enotes_INDUS.repository;

import com.enotes.Enotes_INDUS.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role,Integer> {


    Optional<Role> findByRole(String role);
}
