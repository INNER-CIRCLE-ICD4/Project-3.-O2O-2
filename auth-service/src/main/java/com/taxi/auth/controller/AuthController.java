package com.taxi.auth.controller;

import com.taxi.auth.dto.LoginDto;
import com.taxi.auth.dto.SignupReq;
import com.taxi.auth.dto.TokenDto;
import com.taxi.auth.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup/passenger")
    public ResponseEntity<String> signupPassenger(@RequestBody SignupReq signupReq) {
        authService.signupPassenger(signupReq);
        return ResponseEntity.status(HttpStatus.CREATED).body("Passenger signed up successfully");
    }

    @PostMapping("/signup/driver")
    public ResponseEntity<String> signupDriver(@RequestBody SignupReq signupReq) {
        authService.signupDriver(signupReq);
        return ResponseEntity.status(HttpStatus.CREATED).body("Driver signed up successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto loginDto, HttpServletResponse response) {
        TokenDto tokenDto = authService.login(loginDto.getPhoneNumber(), loginDto.getPassword());

        Cookie refreshTokenCookie = new Cookie("refreshToken", authService.getRefreshToken(loginDto.getPhoneNumber()));
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(refreshTokenCookie);

        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenDto> refresh(HttpServletRequest request) {
        String refreshToken = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("refreshToken")) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        TokenDto newAccessToken = authService.refreshAccessToken(refreshToken);
        return ResponseEntity.ok(newAccessToken);
    }
}
