package com.auctiononline.warbidrestful.services.ilterface;

import com.auctiononline.warbidrestful.payload.request.AuctionRegisterRequest;
import com.auctiononline.warbidrestful.payload.request.AuctionRequest;
import com.auctiononline.warbidrestful.payload.response.GetAllResponse;
import com.auctiononline.warbidrestful.payload.response.MessageResponse;

public interface AuctionService {
    MessageResponse registerAuction(AuctionRegisterRequest auctionRegisterRequest);
    MessageResponse updatePriceIncrease(AuctionRequest auctionRequest);

    GetAllResponse bestAuctionProduct(Long productId);
}
