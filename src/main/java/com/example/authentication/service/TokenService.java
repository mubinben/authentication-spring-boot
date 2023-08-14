package com.example.authentication.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.authentication.entity.AccessToken;
import com.example.authentication.entity.Account;
import com.example.authentication.entity.RefreshToken;
import com.example.authentication.repository.AccessTokenRepository;
import com.example.authentication.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final AccessTokenRepository accessTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.access-token.secret}")
    private String secretAccessToken;
    @Value("${jwt.access-token.life-time}")
    private Long lifeTimeAccessToken;
    @Value("${jwt.access-token.unit-time}")
    private String unitTimeAccessToken;
    @Value("${jwt.refresh-token.secret}")
    private String secretRefreshToken;
    @Value("${jwt.refresh-token.life-time}")
    private Long lifeTimeRefreshToken;
    @Value("${jwt.refresh-token.unit-time}")
    private String unitTimeRefreshToken;

    public AccessToken generateAccessToken(Account account) {
        String token = generateToken(account, secretAccessToken);
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(lifeTimeAccessToken, ChronoUnit.valueOf(unitTimeAccessToken));

        AccessToken accessToken = account.getAccessToken();
        accessToken.setToken(token);
        accessToken.setIssuedAt(issuedAt);
        accessToken.setExpiresAt(expiresAt);
        return accessTokenRepository.save(accessToken);
    }

    public RefreshToken generateRefreshToken(Account account) {
        String token = generateToken(account, secretRefreshToken);
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(lifeTimeRefreshToken, ChronoUnit.valueOf(unitTimeRefreshToken));

        RefreshToken refreshToken = account.getRefreshToken();
        refreshToken.setToken(token);
        refreshToken.setIssuedAt(issuedAt);
        refreshToken.setExpiresAt(expiresAt);
        return refreshTokenRepository.save(refreshToken);
    }

    private String generateToken(Account account, String secret) {
        String subject = account.getEmail();
        String uniqueId = UUID.randomUUID().toString();
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        return JWT.create()
                .withSubject(subject)
                .withClaim("uid", uniqueId)
                .sign(algorithm);
    }

}