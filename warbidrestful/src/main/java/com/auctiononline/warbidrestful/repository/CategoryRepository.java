package com.auctiononline.warbidrestful.repository;

import com.auctiononline.warbidrestful.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query("SELECT c FROM Category c WHERE c.id = :id")
    List<Category> findCategoryById(Integer id);

    @Query("SELECT c FROM Category c WHERE c.name = :name")
    List<Category> findCategoryByName(String name);
}
