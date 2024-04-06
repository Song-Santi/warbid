package com.auctiononline.warbidrestful.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.ResponseEntity;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(AppException ex) {
        ErrorResponse error = new ErrorResponse(ex.getHttpStatus().value(), ex.getMessage());
        return ResponseEntity.status(ex.getHttpStatus()).body(error);
    }

    @Getter
    @AllArgsConstructor
    static class ErrorResponse {
        private final int status;
        private final String message;
    }
}

