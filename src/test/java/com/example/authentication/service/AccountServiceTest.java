package com.example.authentication.service;

import com.example.authentication.entity.Account;
import com.example.authentication.repository.AccountRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    @DisplayName("existsByEmail found")
    public void testExistsByEmail_givenEmail_whenFound_thenReturnTrue() {
        String email = "user@mail.com";
        when(accountRepository.existsByEmail(email)).thenReturn(true);
        boolean actual = accountService.existsByEmail(email);
        assertTrue(actual);
    }

    @Test
    @DisplayName("existsByEmail not found")
    public void testExistsByEmail_givenEmail_whenNotFound_thenReturnFalse() {
        String email = "unknow@mail.com";
        when(accountRepository.existsByEmail(email)).thenReturn(false);
        boolean actual = accountService.existsByEmail(email);
        assertFalse(actual);
    }

    @Test
    @DisplayName("save success")
    public void testSave_givenAccount_whenSuccess_thenReturnAccountSaved() {
        Account account = new Account();
        account.setEmail("user@mail.com");
        account.setPassword("password-encoded");

        Account accountSaved = new Account();
        accountSaved.setId(UUID.randomUUID().toString());
        accountSaved.setEmail("user@mail.com");
        accountSaved.setPassword("password-encoded");

        when(accountRepository.save(account)).thenReturn(accountSaved);

        Account actual = accountService.save(account);

        assertEquals(accountSaved.getId(), actual.getId());
        assertEquals(accountSaved.getEmail(), actual.getEmail());
        assertEquals(accountSaved.getPassword(), actual.getPassword());
    }

    @Test
    @DisplayName("save failure")
    public void testSave_givenAccount_whenFailure_thenThrowConstraintViolationException() {
        Account account = new Account();
        when(accountRepository.save(account)).thenThrow(ConstraintViolationException.class);
        assertThrows(ConstraintViolationException.class, () -> accountService.save(account));
    }

}