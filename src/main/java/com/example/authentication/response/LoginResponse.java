package com.example.authentication.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginResponse {

    private String email;

    private String accessToken;

    private String refreshToken;

}