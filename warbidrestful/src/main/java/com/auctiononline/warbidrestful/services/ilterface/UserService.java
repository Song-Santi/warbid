package com.auctiononline.warbidrestful.services.ilterface;

import com.auctiononline.warbidrestful.models.Role;
import com.auctiononline.warbidrestful.payload.request.EmailForgotRequest;
import com.auctiononline.warbidrestful.payload.request.PasswordRequest;
import com.auctiononline.warbidrestful.payload.request.TokenRequest;
import com.auctiononline.warbidrestful.payload.request.UserRequest;
import com.auctiononline.warbidrestful.payload.response.GetAllResponse;
import com.auctiononline.warbidrestful.payload.response.MessageResponse;

import java.util.Set;

public interface UserService {
    GetAllResponse getAllUser();

    GetAllResponse getAllUserBySearch(String searchTerm);

    MessageResponse update(UserRequest userRequest);

    MessageResponse updateProfile(UserRequest userRequest, Set<Role> roles);
    MessageResponse add(UserRequest userRequest);

    MessageResponse delete(Long id);

    MessageResponse restore(Long id);

    MessageResponse emailSendToken(EmailForgotRequest emailForgotRequest);

    MessageResponse checkToken(TokenRequest tokenRequest);

    MessageResponse changePassword(PasswordRequest passwordRequest);

}
