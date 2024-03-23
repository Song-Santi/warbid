package com.auctiononline.warbidrestful.payload.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class UserInfo {
	private Long id;
	private String username;
	private String email;
	private String phone;
	private String address;
	private List<String> roles;
}
