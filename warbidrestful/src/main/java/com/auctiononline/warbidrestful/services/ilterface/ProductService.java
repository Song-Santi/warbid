package com.auctiononline.warbidrestful.services.ilterface;

import com.auctiononline.warbidrestful.payload.request.ProductRequest;
import com.auctiononline.warbidrestful.payload.response.GetAllResponse;
import com.auctiononline.warbidrestful.payload.response.MessageResponse;

import java.time.LocalDateTime;

public interface ProductService {
    GetAllResponse getAll(int page, int pageSize);

    GetAllResponse getById(Long id);

    GetAllResponse getBySearch(int page, int pageSize, String search);

    GetAllResponse getByCategory(Integer categoryId);

    GetAllResponse getByYetAuction(int page, int pageSize);

    GetAllResponse getByAuctioning(int page, int pageSize);

    GetAllResponse getByAuctioned(int page, int pageSize);

    MessageResponse add(ProductRequest productRequest);

    MessageResponse update(ProductRequest productRequest);

    MessageResponse delete(Long id);
}
