package com.auctiononline.warbidrestful.repository;

import com.auctiononline.warbidrestful.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    @Query("SELECT t FROM Token t WHERE t.user.id = :userId AND t.deleted = :deleted")
    List<Token> findByUserIdAndDeleted(@Param("userId") Long userId, @Param("deleted") Boolean deleted);

}
