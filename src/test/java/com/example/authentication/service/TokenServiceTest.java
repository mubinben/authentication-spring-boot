package com.example.authentication.service;

import com.example.authentication.entity.AccessToken;
import com.example.authentication.entity.Account;
import com.example.authentication.entity.RefreshToken;
import com.example.authentication.repository.AccessTokenRepository;
import com.example.authentication.repository.RefreshTokenRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class TokenServiceTest {

    @Mock
    private AccessTokenRepository accessTokenRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private TokenService tokenService;

    @Test
    @DisplayName("generateAccessToken success")
    void testGenerateAccessToken_givenAccount_whenSuccess_thenReturnAccessToken() {
        ReflectionTestUtils.setField(tokenService, "secretAccessToken", "mock-secret");
        ReflectionTestUtils.setField(tokenService, "lifeTimeAccessToken", 15L);
        ReflectionTestUtils.setField(tokenService, "unitTimeAccessToken", "MINUTES");

        AccessToken accessToken = new AccessToken();
        accessToken.setId(1L);
        Account account = new Account();
        account.setId(UUID.randomUUID().toString());
        account.setEmail("test@mail.com");
        account.setPassword("password-encoded");
        account.setAccessToken(accessToken);

        AccessToken accessTokenSaved = new AccessToken();
        accessTokenSaved.setToken("mock.access.token");
        accessTokenSaved.setIssuedAt(Instant.now());
        accessTokenSaved.setExpiresAt(Instant.now().plus(15, ChronoUnit.MINUTES));
        when(accessTokenRepository.save(accessToken)).thenReturn(accessTokenSaved);

        AccessToken actual = tokenService.generateAccessToken(account);

        assertEquals(accessTokenSaved.getToken(), actual.getToken());
        assertEquals(accessTokenSaved.getIssuedAt(), actual.getIssuedAt());
        assertEquals(accessTokenSaved.getExpiresAt(), actual.getExpiresAt());
    }

    @Test
    @DisplayName("generateRefreshToken success")
    void testGenerateRefreshToken_givenAccount_whenSuccess_thenReturnRefreshToken() {
        ReflectionTestUtils.setField(tokenService, "secretRefreshToken", "mock-secret");
        ReflectionTestUtils.setField(tokenService, "lifeTimeRefreshToken", 1L);
        ReflectionTestUtils.setField(tokenService, "unitTimeRefreshToken", "DAYS");

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(1L);
        Account account = new Account();
        account.setId(UUID.randomUUID().toString());
        account.setEmail("test@mail.com");
        account.setPassword("password-encoded");
        account.setRefreshToken(refreshToken);

        RefreshToken refreshTokenSaved = new RefreshToken();
        refreshTokenSaved.setToken("mock.access.token");
        refreshTokenSaved.setIssuedAt(Instant.now());
        refreshTokenSaved.setExpiresAt(Instant.now().plus(1, ChronoUnit.DAYS));
        when(refreshTokenRepository.save(refreshToken)).thenReturn(refreshTokenSaved);

        RefreshToken actual = tokenService.generateRefreshToken(account);

        assertEquals(refreshTokenSaved.getToken(), actual.getToken());
        assertEquals(refreshTokenSaved.getIssuedAt(), actual.getIssuedAt());
        assertEquals(refreshTokenSaved.getExpiresAt(), actual.getExpiresAt());
    }

}