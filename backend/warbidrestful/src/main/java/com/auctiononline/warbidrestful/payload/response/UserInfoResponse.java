package com.auctiononline.warbidrestful.payload.response;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class UserInfoResponse {
    private Integer code;
    private String status;
    private String message;
    private UserInfo data;
}
