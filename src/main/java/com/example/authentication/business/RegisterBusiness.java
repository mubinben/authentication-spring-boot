package com.example.authentication.business;

import com.example.authentication.entity.AccessToken;
import com.example.authentication.entity.Account;
import com.example.authentication.entity.RefreshToken;
import com.example.authentication.exception.ConflictException;
import com.example.authentication.request.RegisterRequest;
import com.example.authentication.response.RegisterResponse;
import com.example.authentication.service.AccountService;
import com.example.authentication.validate.RegisterValidate;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegisterBusiness {

    private final RegisterValidate registerValidate;
    private final AccountService accountService;

    @Qualifier("passwordEncoder")
    private BCryptPasswordEncoder passwordEncoder;

    public RegisterResponse register(RegisterRequest request) {
        registerValidate.validate(request);

        boolean isDuplicate = accountService.existsByEmail(request.getEmail());
        if (isDuplicate) {
            throw new ConflictException(String.format("email %s duplicate", request.getEmail()));
        }

        String passwordEncoded = passwordEncoder.encode(request.getPassword());

        Account account = new Account();
        account.setEmail(request.getEmail());
        account.setPassword(passwordEncoded);
        account.setAccessToken(new AccessToken());
        account.setRefreshToken(new RefreshToken());
        Account saved = accountService.save(account);

        RegisterResponse response = new RegisterResponse();
        response.setEmail(saved.getEmail());
        return response;
    }

}