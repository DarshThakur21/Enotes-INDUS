package com.enotes.Enotes_INDUS.repository;

import com.enotes.Enotes_INDUS.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TodoRepo extends JpaRepository<Todo,Integer> {


    @Query("select t from Todo t where t.createdBy=:createdBy ")
    List<Todo> findByCreatedBy(@Param("createdBy") int userId);
}
