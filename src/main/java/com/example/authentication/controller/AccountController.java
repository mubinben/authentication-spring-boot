package com.example.authentication.controller;

import com.example.authentication.business.LoginBusiness;
import com.example.authentication.business.RegisterBusiness;
import com.example.authentication.request.LoginRequest;
import com.example.authentication.request.RegisterRequest;
import com.example.authentication.response.LoginResponse;
import com.example.authentication.response.RegisterResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/account")
@AllArgsConstructor
public class AccountController {

    private final RegisterBusiness registerBusiness;
    private final LoginBusiness loginBusiness;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        RegisterResponse response = registerBusiness.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = loginBusiness.login(request);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.noContent().build();
    }

}