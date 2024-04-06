package com.auctiononline.warbidrestful.repository;

import com.auctiononline.warbidrestful.models.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {
    @Query("SELECT a FROM Auction a WHERE a.productId = :productId ORDER BY a.bidAmount DESC")
    List<Auction> findAllByProductIdOrderByBidAmountDesc(Long productId);
}
