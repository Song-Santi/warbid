package com.auctiononline.warbidrestful.services.ilterface;

import com.auctiononline.warbidrestful.payload.request.UserRequest;
import com.auctiononline.warbidrestful.payload.response.GetAllUserResponse;
import com.auctiononline.warbidrestful.payload.response.MessageResponse;

public interface UserService {
    GetAllUserResponse getAllUser();

    GetAllUserResponse getAllUserBySearch(String searchTerm);

    MessageResponse update(UserRequest userRequest);
    MessageResponse add(UserRequest userRequest);

    MessageResponse delete(Long id);

    MessageResponse restore(Long id);

}
