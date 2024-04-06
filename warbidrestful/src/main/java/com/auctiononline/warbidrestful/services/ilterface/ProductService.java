package com.auctiononline.warbidrestful.services.ilterface;

import com.auctiononline.warbidrestful.payload.request.ProductRequest;
import com.auctiononline.warbidrestful.payload.response.GetAllResponse;
import com.auctiononline.warbidrestful.payload.response.MessageResponse;

import java.time.LocalDateTime;

public interface ProductService {
    GetAllResponse getAll();

    GetAllResponse getById(Long id);

    GetAllResponse getBySearch(String search);

    GetAllResponse getByCategory(Integer categoryId);

    GetAllResponse getByYetAuction();

    GetAllResponse getByAuctioning();

    GetAllResponse getByAuctioned();

    MessageResponse add(ProductRequest productRequest);

    MessageResponse update(ProductRequest productRequest);

    MessageResponse delete(Long id);
}
