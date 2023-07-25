package com.example.authentication.service;

import com.example.authentication.entity.Account;
import com.example.authentication.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public boolean existsByEmail(String email) {
        return accountRepository.existsByEmail(email);
    }

    public Account save(Account account) {
        return accountRepository.save(account);
    }

}