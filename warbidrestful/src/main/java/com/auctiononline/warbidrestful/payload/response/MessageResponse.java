package com.auctiononline.warbidrestful.payload.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Data
//@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class MessageResponse {
	private int code;
	private HttpStatus status;
	private String message;
	private Map<String, String> detailed;

	public MessageResponse(int code, HttpStatus status, String message) {
		this.code = code;
		this.status = status;
		this.message = message;
	}

	public MessageResponse(int code, HttpStatus status, String message, Map<String, String> detailed) {
		this.code = code;
		this.status = status;
		this.message = message;
		this.detailed = detailed;
	}

}
