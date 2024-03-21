package com.auctiononline.warbidrestful.payload.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
//@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class MessageResponse {
	private int code;
	private HttpStatus status;
	private String message;

	public MessageResponse(int code, HttpStatus status, String message) {
		this.code = code;
		this.status = status;
		this.message = message;
	}

}
