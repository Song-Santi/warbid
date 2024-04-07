package com.auctiononline.warbidrestful.services.ilterface;

import com.auctiononline.warbidrestful.models.Role;
import com.auctiononline.warbidrestful.payload.request.*;
import com.auctiononline.warbidrestful.payload.response.GetAllResponse;
import com.auctiononline.warbidrestful.payload.response.MessageResponse;

import java.util.Set;

public interface UserService {
    GetAllResponse getAllUser(int page, int pageSize);

    GetAllResponse getAllUserBySearch(int page, int pageSize, String searchTerm);

    MessageResponse update(UserRequest userRequest);

    MessageResponse updateProfile(UserRequest userRequest, Set<Role> roles);
    MessageResponse add(UserRequest userRequest);

    MessageResponse delete(Long id);

    MessageResponse restore(Long id);

    MessageResponse emailSendToken(EmailForgotRequest emailForgotRequest);

    MessageResponse checkToken(TokenRequest tokenRequest);

    MessageResponse changePassDueForgot(ChangePassDueForgotRequest changePassDueForgotRequest);

    MessageResponse changePassword(PasswordRequest passwordRequest);

}
