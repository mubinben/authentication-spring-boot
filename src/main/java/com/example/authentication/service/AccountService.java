package com.example.authentication.service;

import com.example.authentication.entity.Account;
import com.example.authentication.exception.UnauthorizedException;
import com.example.authentication.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    @Qualifier("passwordEncoder")
    private BCryptPasswordEncoder passwordEncoder;

    public boolean existsByEmail(String email) {
        return accountRepository.existsByEmail(email);
    }

    public Account save(Account account) {
        return accountRepository.save(account);
    }

    public Account authenticate(String email, String password) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("email or password invalid"));
        boolean isMatch = passwordEncoder.matches(password, account.getPassword());
        if (!isMatch) {
            throw new UnauthorizedException("email or password invalid");
        }
        return account;
    }

}