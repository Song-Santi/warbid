package com.auctiononline.warbidrestful.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "auctions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Auction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "bid_amount")
    private BigDecimal bidAmount;

    @Column(name = "bid_time")
    private LocalDateTime bidTime;

    @Column(name = "bid_status")
    private Boolean bidStatus;

    public Auction(Long productId, Long userId, Boolean bidStatus){
        this.productId = productId;
        this.userId = userId;
        this.bidStatus = bidStatus;
    }
}
