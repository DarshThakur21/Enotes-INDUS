package com.enotes.Enotes_INDUS.repository;

import com.enotes.Enotes_INDUS.model.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountStatuRepo extends JpaRepository<AccountStatus,Integer> {



    @Query("SELECT a.id from AccountStatus a where a.verificationCode= :code")
    Integer findByCode(@Param("code") String code);
}
