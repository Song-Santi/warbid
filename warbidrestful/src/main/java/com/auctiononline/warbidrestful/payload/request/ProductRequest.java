package com.auctiononline.warbidrestful.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private Long id;
    private String title;
    private String description;
    private String image;
    private Integer categoryId;
    private BigDecimal startPrice;
    private String auctionTime;
    private String auctionEndTime;

    public ProductRequest(String title, String description, String image, Integer categoryId, BigDecimal startPrice, String auctionTime, String auctionEndTime){
        this.title = title;
        this.description = description;
        this.image = image;
        this.categoryId = categoryId;
        this.startPrice = startPrice;
        this.auctionTime = auctionTime;
        this.auctionEndTime = auctionEndTime;
    }

}
