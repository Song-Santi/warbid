package com.auctiononline.warbidrestful.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String title;
    private String description;
    private String image;
    private String category;
    private BigDecimal startPrice;
    private LocalDateTime auctionTime;
    private LocalDateTime auctionEndTime;
}
