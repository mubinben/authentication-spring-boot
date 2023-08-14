package com.example.authentication.business;

import com.example.authentication.entity.AccessToken;
import com.example.authentication.entity.Account;
import com.example.authentication.entity.RefreshToken;
import com.example.authentication.request.LoginRequest;
import com.example.authentication.response.LoginResponse;
import com.example.authentication.service.AccountService;
import com.example.authentication.service.TokenService;
import com.example.authentication.validate.LoginValidate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoginBusiness {

    private final LoginValidate loginValidate;
    private final AccountService accountService;
    private final TokenService tokenService;

    public LoginResponse login(LoginRequest request) {
        loginValidate.validate(request);

        Account account = accountService.authenticate(request.getEmail(), request.getPassword());

        AccessToken accessToken = tokenService.generateAccessToken(account);
        RefreshToken refreshToken = tokenService.generateRefreshToken(account);

        LoginResponse response = new LoginResponse();
        response.setEmail(account.getEmail());
        response.setAccessToken(accessToken.getToken());
        response.setRefreshToken(refreshToken.getToken());
        return response;
    }

}