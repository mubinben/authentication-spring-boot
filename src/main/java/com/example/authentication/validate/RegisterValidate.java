package com.example.authentication.validate;


import com.example.authentication.exception.BadRequestException;
import com.example.authentication.request.RegisterRequest;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RegisterValidate {

    public void validate(RegisterRequest request) {
        validateEmail(request.getEmail());
        validatePassword(request.getPassword(), request.getConfirmPassword());
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

    private void validatePassword(String password, String confirmPassword) {
        if (password == null) {
            throw new BadRequestException("password must not null");
        }

        if (password.isBlank()) {
            throw new BadRequestException("password must not blank");
        }

        if (confirmPassword == null) {
            throw new BadRequestException("confirm password must not null");
        }

        if (confirmPassword.isBlank()) {
            throw new BadRequestException("confirm password must not blank");
        }

        if (!password.equals(confirmPassword)) {
            throw new BadRequestException("password not match");
        }
    }

}