package com.taxi.auth.service;

import com.taxi.auth.dto.TokenDto;
import com.taxi.auth.exception.InvalidTokenException;
import com.taxi.auth.jwt.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    private final Map<String, String> refreshTokenStorage = new ConcurrentHashMap<>();

    @Transactional
    public TokenDto
    login(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        TokenDto tokenDto = jwtTokenProvider.generateToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);
//        refreshTokenStorage.put(authentication.getName(), refreshToken);
        return tokenDto;
    }

    @Transactional
    public TokenDto refreshAccessToken(String refreshToken) {
        if (refreshToken == null) {
            throw new InvalidTokenException("Refresh Token이 없습니다.");
        }

        jwtTokenProvider.validateToken(refreshToken);

        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);

        String storedRefreshToken = refreshTokenStorage.get(authentication.getName());
        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            throw new InvalidTokenException("저장된 Refresh Token과 일치하지 않습니다.");
        }

        return jwtTokenProvider.generateToken(authentication);
    }

    public String getRefreshToken(String username) {
        return null;
    }
}
