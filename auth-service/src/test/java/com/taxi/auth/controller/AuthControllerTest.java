package com.taxi.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taxi.auth.dto.LoginDto;
import com.taxi.auth.dto.SignupReq;
import com.taxi.auth.dto.TokenDto;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerTest {
    private static final String PASSENGER_PHONE_NUMBER = "010-1111-1111";
    private static final String DRIVER_PHONE_NUMBER = "010-2222-2222";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private SignupReq passengerReg;
    private SignupReq driverReq;

    @BeforeEach
    void setUp() throws Exception {
        passengerReg = SignupReq.builder()
                .name("승객1")
                .email("passenger@test.com")
                .phoneNumber(PASSENGER_PHONE_NUMBER)
                .password("password123")
            .build();

        driverReq = SignupReq.builder()
                .name("드라이버1")
                .email("driver@test.com")
                .phoneNumber(DRIVER_PHONE_NUMBER)
                .password("password123")
                .licenseNumber("11-22-333333-44")
                .vehicleNumber("12가3456")
                .vehicleModel("K5")
                .vehicleType("SEDAN")
            .build();
    }

    @Nested
    @DisplayName("회원가입 테스트")
    class SignupTests {

        @Test
        @DisplayName("성공: 승객 회원가입")
        void signupPassenger_Success() throws Exception {
            mockMvc.perform(post("/api/auth/signup/passenger")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(passengerReg)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Passenger signed up successfully"))
                .andDo(print());
        }

        @Test
        @DisplayName("성공: 드라이버 회원가입")
        void signupDriver_Success() throws Exception {
            mockMvc.perform(post("/api/auth/signup/driver")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(driverReq)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Driver signed up successfully"))
                .andDo(print());
        }

        @Test
        @DisplayName("실패: 중복된 전화번호로 회원가입")
        void signup_Fail_DuplicatePhoneNumber() throws Exception {
            signupUser(passengerReg, "passenger");

            SignupReq duplicateRequestDto = SignupReq.builder()
                    .name("드라이버1")
                    .email("driver@test.com")
                    .phoneNumber(PASSENGER_PHONE_NUMBER)
                    .password("password123")
                    .licenseNumber("11-22-333333-44")
                    .vehicleNumber("12가3456")
                    .vehicleModel("K5")
                    .vehicleType("SEDAN")
                .build();

            mockMvc.perform(post("/api/auth/signup/driver")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(duplicateRequestDto)))
                .andExpect(status().isBadRequest())
                .andDo(print());
        }
    }

    @Nested
    @DisplayName("로그인 테스트")
    class LoginTests {

        @Test
        @DisplayName("성공: 올바른 전화번호와 비밀번호로 로그인")
        void login_Success() throws Exception {
            // 1. 테스트용 사용자 회원가입
            SignupReq signupDto = SignupReq.builder()
                    .name("로그인테스트")
                    .email("login@test.com")
                    .phoneNumber("010-3333-3333")
                    .password("password123")
                .build();

            signupUser(passengerReg, "passenger");

            // 2. 로그인 시도
            LoginDto loginDto = new LoginDto(PASSENGER_PHONE_NUMBER, "password123");

            mockMvc.perform(post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.grantType").value("Bearer"))
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(cookie().exists("refreshToken"))
                .andDo(print());
        }

        @Test
        @DisplayName("실패: 잘못된 비밀번호로 로그인")
        void login_Fail_WrongPassword() throws Exception {
            // 1. 테스트용 사용자 회원가입
            SignupReq signupDto = SignupReq.builder()
                    .name("로그인테스트")
                    .email("login@test.com")
                    .phoneNumber("010-4444-4444")
                    .password("password123")
                .build();
            signupUser(passengerReg, "passenger");

            // 2. 잘못된 비밀번호로 로그인 시도
            LoginDto loginDto = new LoginDto(PASSENGER_PHONE_NUMBER, "wrong-password");

            mockMvc.perform(post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isUnauthorized()) // 401 Unauthorized (BadCredentialsException)
                .andDo(print());
        }
    }

    @Nested
    @DisplayName("보호된 API 접근 테스트")
    class ProtectedApiTests {

        @Test
        @DisplayName("성공: 유효한 토큰으로 보호된 API 접근")
        void accessProtectedApi_Success() throws Exception {
            // 1. 사용자 가입 및 로그인하여 토큰 획득
            TokenDto tokenDto = signupAndLogin("api_user", "010-5555-5555", "password123");

            // 2. 보호된 API에 토큰과 함께 요청
            // SecurityConfig에 따라 인증이 필요한 아무 경로 (/api/auth/**, /h2-console/** 제외)
            mockMvc.perform(get("/api/any-protected-path")
                    .header("Authorization", "Bearer " + tokenDto.getAccessToken()))
                .andExpect(status().isNotFound()) // 401/403이 아니면 인증 성공
                .andDo(print());
        }

        @Test
        @DisplayName("실패: 토큰 없이 보호된 API 접근")
        void accessProtectedApi_Fail_NoToken() throws Exception {
            mockMvc.perform(get("/api/any-protected-path"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
        }
    }

    private void signupUser(SignupReq requestDto, String userType) throws Exception {
        String url = "/api/auth/signup/" + userType;
        mockMvc.perform(post(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDto)));
    }

    private TokenDto signupAndLogin(String name, String phoneNumber, String password) throws Exception {
        // 회원가입
        SignupReq signupDto = SignupReq.builder()
                .name(name)
                .email(name + "@test.com")
                .phoneNumber(phoneNumber)
                .password(password)
            .build();
        signupUser(signupDto, "passenger");

        // 로그인
        LoginDto loginDto = new LoginDto(phoneNumber, password);
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)))
            .andReturn();

        // 토큰 DTO 반환
        String responseBody = result.getResponse().getContentAsString();
        return objectMapper.readValue(responseBody, TokenDto.class);
    }
}
