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
	private String username;
	private String token;
	private List<String> roles;
}
