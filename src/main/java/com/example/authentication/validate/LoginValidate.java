package com.example.authentication.validate;

import com.example.authentication.exception.BadRequestException;
import com.example.authentication.request.LoginRequest;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class LoginValidate {

    public void validate(LoginRequest request) {
        validateEmail(request.getEmail());
        validatePassword(request.getPassword());
    }

    private void validateEmail(String email) {
        if (email == null) {
            throw new BadRequestException("email must not null");
        }

        if (email.isBlank()) {
            throw new BadRequestException("email must not blank");
        }

        Pattern emailPattern = Pattern.compile("^[a-z]+[a-z0-9]*@[a-z0-9]+.[a-z(.)]{2,}$");
        Matcher emailMatcher = emailPattern.matcher(email);
        if (!emailMatcher.matches()) {
            throw new BadRequestException("email invalid format");
        }
    }

    private void validatePassword(String password) {
        if (password == null) {
            throw new BadRequestException("password must not null");
        }

        if (password.isBlank()) {
            throw new BadRequestException("password must not blank");
        }
    }

}