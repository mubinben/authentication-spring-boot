package com.example.authentication.advice;

import com.example.authentication.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.util.Date;

@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException ex) {
        String errorDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        HttpStatus httpStatus = HttpStatus.valueOf(ex.getStatusCode().value());

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(errorDateTime);
        errorResponse.setStatus(httpStatus.value());
        errorResponse.setError(httpStatus.getReasonPhrase());
        errorResponse.setMessage(ex.getReason());

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

}