package com.auctiononline.warbidrestful.repository;

import com.auctiononline.warbidrestful.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p WHERE p.deleted = false")
    List<Post> findAllActivePosts();

    @Query("SELECT p FROM Post p WHERE p.deleted = false AND p.id = :id")
    List<Post> findPostActiveById(Long id);
}
