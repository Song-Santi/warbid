package com.auctiononline.warbidrestful.services.ilterface;

import com.auctiononline.warbidrestful.models.Category;
import com.auctiononline.warbidrestful.payload.response.GetAllResponse;
import com.auctiononline.warbidrestful.payload.response.MessageResponse;

import java.util.List;

public interface CategoryService {
    GetAllResponse getAll(int page, int pageSize);
    GetAllResponse getById(Integer id);

    GetAllResponse getByName(String name);

    MessageResponse add(Category category);

    MessageResponse update(Category category);

}
