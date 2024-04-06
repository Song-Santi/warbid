package com.auctiononline.warbidrestful.repository;

import java.util.List;
import java.util.Optional;

import com.auctiononline.warbidrestful.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  @Query("SELECT u FROM User u WHERE u.deleted = false")
  List<User> findAllActiveUsers();

  @Query("SELECT DISTINCT u FROM User u " +
          "WHERE u.deleted = false AND " +
          "(LOWER(u.username) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
          "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
          "LOWER(u.phone) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
          "LOWER(u.address) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
  List<User> searchUsersByKeyword(@Param("searchTerm") String searchTerm);

  @Query("SELECT u FROM User u WHERE u.deleted = FALSE AND u.id = :id")
  List<User> findByIdActive(Long id);

  @Query("SELECT u FROM User u WHERE u.deleted = FALSE AND u.username = :username")
  Optional<User> findByUsername(String username);

  @Query("SELECT u FROM User u WHERE u.deleted = FALSE AND u.email = :email")
  Optional<User> findByEmail(String email);

  @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM User u WHERE u.username = :username AND u.deleted = FALSE")
  Boolean existsActiveByUsername(String username);


  @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.username = :username AND u.deleted = FALSE")
  Boolean existsByUsername(String username);

  @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email = :email AND u.deleted = FALSE")
  Boolean existsByEmail(String email);

  @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.username = :username AND u.id <> :userId AND u.deleted = FALSE")
  Boolean existsByUsernameAndDifferentId(@Param("username") String username, @Param("userId") Long userId);

  @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email = :email AND u.id <> :userId AND u.deleted = FALSE")
  Boolean existsByEmailAndDifferentId(@Param("email") String email, @Param("userId") Long userId);
}
