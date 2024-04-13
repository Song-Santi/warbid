package com.auctiononline.warbidrestful.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuctionRequest {
    private Long productId;
    private Long userId;
    private BigDecimal bidAmount;
    private String bidTime;
}
