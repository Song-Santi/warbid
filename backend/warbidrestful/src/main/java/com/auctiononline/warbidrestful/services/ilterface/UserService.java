package com.auctiononline.warbidrestful.services.ilterface;

import com.auctiononline.warbidrestful.models.Role;
import com.auctiononline.warbidrestful.payload.request.UserRequest;
import com.auctiononline.warbidrestful.payload.response.GetAllUserResponse;
import com.auctiononline.warbidrestful.payload.response.MessageResponse;

import java.util.Set;

public interface UserService {
    GetAllUserResponse getAllUser();

    GetAllUserResponse getAllUserBySearch(String searchTerm);

    MessageResponse update(UserRequest userRequest);

    MessageResponse updateProfile(UserRequest userRequest, Set<Role> roles);
    MessageResponse add(UserRequest userRequest);

    MessageResponse delete(Long id);

    MessageResponse restore(Long id);

}
