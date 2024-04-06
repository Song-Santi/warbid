package com.auctiononline.warbidrestful.repository;

import com.auctiononline.warbidrestful.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE p.deleted = FALSE")
    List<Product> getAllProductActive();

    @Query("SELECT p FROM Product p WHERE p.deleted = FALSE AND p.id = :id")
    List<Product> getAllProductActiveById(Long id);

    @Query("SELECT p FROM Product p WHERE p.deleted = FALSE AND p.categoryId = :categoryId")
    List<Product> getAllProductActiveByCategory(Integer categoryId);

    //chua đấu giá
    @Query("SELECT p FROM Product p WHERE p.deleted = FALSE AND p.auctionTime > :currentTime")
    List<Product> getAllActiveProductsNotAuctioned(@Param("currentTime") LocalDateTime currentTime);

//    //Đã đấu giá
    @Query("SELECT p FROM Product p WHERE p.deleted = FALSE AND p.auctionEndTime < :currentTime")
    List<Product> getAllActiveProductsAuctioned(@Param("currentTime") LocalDateTime currentTime);

//    //Đag đấu giá
    @Query("SELECT p FROM Product p WHERE p.deleted = FALSE AND p.auctionTime < :currentTime AND p.auctionEndTime > :currentTime")
    List<Product> getAllActiveProductsAuctioning(@Param("currentTime") LocalDateTime currentTime);

    @Query("SELECT p FROM Product p " +
            "WHERE p.deleted = FALSE AND " +
            "(LOWER(p.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(CAST(p.startPrice AS string)) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Product> searchProductByKeyword(@Param("searchTerm") String searchTerm);


}
