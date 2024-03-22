package com.auctiononline.warbidrestful.repository;

import java.util.Optional;

import com.auctiononline.warbidrestful.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);

  @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.username = :username AND u.id <> :userId")
  Boolean existsByUsernameAndDifferentId(@Param("username") String username, @Param("userId") Long userId);

  @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email = :email AND u.id <> :userId")
  Boolean existsByEmailAndDifferentId(@Param("email") String email, @Param("userId") Long userId);
}
