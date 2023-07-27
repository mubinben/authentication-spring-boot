package com.example.authentication.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResponse {

    private String timestamp;

    private String error;

    private int status;

    private String message;

}