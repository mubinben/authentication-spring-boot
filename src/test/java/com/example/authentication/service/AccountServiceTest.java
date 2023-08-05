package com.example.authentication.service;

import com.example.authentication.entity.Account;
import com.example.authentication.exception.UnauthorizedException;
import com.example.authentication.repository.AccountRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
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

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

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

    @Test
    @DisplayName("authenticate email not found")
    public void testAuthenticate_givenEmail_whenNotFound_thenThrowUnauthorizedException() {
        String email = "unknow@mail.com";
        String password = "random";

        Optional<Account> emptyOpt = Optional.empty();
        when(accountRepository.findByEmail(email)).thenReturn(emptyOpt);

        UnauthorizedException actual = assertThrows(
                UnauthorizedException.class,
                () -> accountService.authenticate(email, password)
        );

        assertEquals(HttpStatus.UNAUTHORIZED, actual.getStatusCode());
        assertEquals("email or password invalid", actual.getReason());
    }

    @Test
    @DisplayName("authenticate password not match")
    public void testAuthenticate_givenPassword_whenNotMatch_thenThrowUnauthorizedException() {
        String email = "test@mail.com";
        String password = "random";

        Account account = new Account();
        account.setId(UUID.randomUUID().toString());
        account.setEmail(email);
        account.setPassword("password-encoded");
        Optional<Account> accountOpt = Optional.of(account);
        when(accountRepository.findByEmail(email)).thenReturn(accountOpt);

        when(passwordEncoder.matches(password, account.getPassword())).thenReturn(false);

        UnauthorizedException actual = assertThrows(
                UnauthorizedException.class,
                () -> accountService.authenticate(email, password)
        );

        assertEquals(HttpStatus.UNAUTHORIZED, actual.getStatusCode());
        assertEquals("email or password invalid", actual.getReason());
    }

    @Test
    @DisplayName("authenticate success")
    public void testAuthenticate_givenEmailAndPassword_whenSuccess_thenAccount() {
        String email = "test@mail.com";
        String password = "secret";

        Account account = new Account();
        account.setId(UUID.randomUUID().toString());
        account.setEmail(email);
        account.setPassword("password-encoded");
        Optional<Account> accountOpt = Optional.of(account);
        when(accountRepository.findByEmail(email)).thenReturn(accountOpt);

        when(passwordEncoder.matches(password, account.getPassword())).thenReturn(true);

        Account actual = accountService.authenticate(email, password);

        assertEquals(account.getId(), actual.getId());
        assertEquals(account.getEmail(), actual.getEmail());
        assertEquals(account.getPassword(), actual.getPassword());
    }

}