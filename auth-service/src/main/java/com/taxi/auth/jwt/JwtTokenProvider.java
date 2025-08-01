package com.taxi.auth.jwt;

import com.taxi.auth.dto.TokenDto;
import com.taxi.auth.exception.InvalidTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    private static final long ONE_MINUTES = 60_000;

    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "Bearer";

    private static final String INVALID_SIGNATURE_JWT_MESSAGE = "잘못된 JWT 서명입니다.";
    private static final String EXPIRED_JWT_MESSAGE = "만료된 JWT 토큰입니다.";
    private static final String UNSUPPORTED_JWT_MESSAGE = "지원되지 않는 JWT 토큰입니다.";
    private static final String INVALID_JWT_MESSAGE = "JWT 토큰이 잘못되었습니다.";
    private static final String NO_AUTHORITY_MESSAGE = "권한 정보가 없는 토큰입니다.";

    private final SecretKey key;
    private final long accessTokenExpirationMillisec;
    private final long refreshTokenExpirationMillisec;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey,
                            @Value("${jwt.access-token-expiration-minutes}") long accessTokenExpirationMinutes,
                            @Value("${jwt.refresh-token-expiration-days}") long refreshTokenExpirationDays) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpirationMillisec = accessTokenExpirationMinutes * ONE_MINUTES;
        this.refreshTokenExpirationMillisec = refreshTokenExpirationDays * 24 * 60 * ONE_MINUTES;
    }

    public TokenDto generateToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        Date accessTokenExpiratiionDate = new Date(now + accessTokenExpirationMillisec);
        String accessToken = Jwts.builder()
            .setSubject(authentication.getName())
            .claim(AUTHORITIES_KEY, authorities)
            .expiration(accessTokenExpiratiionDate)
            .signWith(key)
            .compact();

        return TokenDto.builder()
            .grantType(BEARER_TYPE)
            .accessToken(accessToken)
            .build();
    }

    public String generateRefreshToken(Authentication authentication) {
        long now = (new Date()).getTime();
        Date refreshTokenExpirationDate = new Date(now + refreshTokenExpirationMillisec);

        return Jwts.builder()
            .setSubject(authentication.getName())
            .expiration(refreshTokenExpirationDate)
            .signWith(key)
            .compact();

    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        Collection<? extends GrantedAuthority> authorities =
            Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public void validateToken(String token) {
        try {
            Jwts.parser().verifyWith(this.key).build().parseSignedClaims(token);
        }catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            throw new InvalidTokenException(INVALID_SIGNATURE_JWT_MESSAGE);
        }catch (ExpiredJwtException e) {
            throw new InvalidTokenException(EXPIRED_JWT_MESSAGE);
        }catch (UnsupportedJwtException e) {
            throw new InvalidTokenException(UNSUPPORTED_JWT_MESSAGE);
        }catch (IllegalArgumentException e) {
            throw new InvalidTokenException(INVALID_JWT_MESSAGE);
        }catch(JwtException e) {
            throw new RuntimeException();
        }
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parser().verifyWith(this.key).build().parseSignedClaims(accessToken).getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public Long getExpiration(String accessToken) {
        Date expiration = Jwts.parser().verifyWith(this.key).build()
            .parseSignedClaims(accessToken)
            .getPayload()
            .getExpiration();
        long now = new Date().getTime();
        return (expiration.getTime() - now);
    }
}
