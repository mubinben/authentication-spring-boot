package com.example.authentication.business;

import com.example.authentication.entity.Account;
import com.example.authentication.request.LoginRequest;
import com.example.authentication.response.LoginResponse;
import com.example.authentication.service.AccountService;
import com.example.authentication.validate.LoginValidate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoginBusiness {

    private final LoginValidate loginValidate;
    private final AccountService accountService;

    public LoginResponse login(LoginRequest request) {
        loginValidate.validate(request);

        Account account = accountService.authenticate(request.getEmail(), request.getPassword());

        LoginResponse response = new LoginResponse();
        response.setEmail(account.getEmail());
        return response;
    }

}