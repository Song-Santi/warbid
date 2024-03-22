package com.auctiononline.warbidrestful.payload.response;

import com.auctiononline.warbidrestful.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetAllUserResponse {
    private int code;
    private String status;
    private String message;
    private List<User> data;
}
