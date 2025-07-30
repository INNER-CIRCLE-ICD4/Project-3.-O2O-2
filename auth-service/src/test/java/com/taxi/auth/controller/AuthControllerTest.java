package com.taxi.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taxi.auth.dto.LoginDto;
import com.taxi.auth.dto.SignupReq;
import com.taxi.auth.AuthServiceApplication;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private SignupReq passengerReg;
    private SignupReq driverReq;

    @BeforeEach
    void setUp() {
        // 각 테스트 전에 사용할 DTO 객체 초기화
        passengerReg = new SignupReq();
        // 리플렉션을 사용하거나 setter를 추가하여 필드 값을 설정할 수 있습니다.
        // 테스트를 위해 임시로 public 필드로 만들거나, 리플렉션 유틸리티를 사용하는 것이 일반적입니다.
        // 여기서는 테스트의 편의를 위해 DTO에 setter를 추가했다고 가정합니다.
        // (실제 코드에서는 SignupRequestDto에 setter를 추가해야 합니다)
        // passengerSignupRequest.setEmail("passenger@test.com");
        // ...

        driverReq = new SignupReq();
        // driverSignupRequest.setEmail("driver@test.com");
        // ...
    }


    // SignupRequestDto에 필드 값을 설정하기 위한 간단한 유틸리티 메서드
    // 실제로는 리플렉션이나 다른 라이브러리를 사용할 수 있습니다.
    private String createSignupJson(String name, String email, String password, String userType) {
        if ("passenger".equals(userType)) {
            return String.format("{\"name\":\"%s\", \"email\":\"%s\", \"password\":\"%s\", \"phoneNumber\":\"010-1234-5678\"}", name, email, password);
        } else {
            return String.format("{\"name\":\"%s\", \"email\":\"%s\", \"password\":\"%s\", \"phoneNumber\":\"010-9876-5432\", \"licenseNumber\":\"12-3456\", \"vehicleNumber\":\"11가1111\", \"vehicleModel\":\"K5\", \"vehicleType\":\"SEDAN\"}", name, email, password);
        }
    }


    @Test
    @DisplayName("승객 회원가입 성공")
    void signupPassenger_Success() throws Exception {
        String signupJson = createSignupJson("test_passenger", "passenger@test.com", "password123", "passenger");

        mockMvc.perform(post("/api/auth/signup/passenger")
                .contentType(MediaType.APPLICATION_JSON)
                .content(signupJson))
            .andExpect(status().isCreated()) // 201 Created 상태 코드 확인
            .andExpect(content().string("Passenger signed up successfully"))
            .andDo(print()); // 요청/응답 전체 내용 출력
    }

    @Test
    @DisplayName("드라이버 회원가입 성공")
    void signupDriver_Success() throws Exception {
        String signupJson = createSignupJson("test_driver", "driver@test.com", "password123", "driver");

        mockMvc.perform(post("/api/auth/signup/driver")
                .contentType(MediaType.APPLICATION_JSON)
                .content(signupJson))
            .andExpect(status().isCreated())
            .andExpect(content().string("Driver signed up successfully"))
            .andDo(print());
    }

    @Test
    @DisplayName("중복된 이메일로 회원가입 시 실패")
    void signup_Fail_DuplicateEmail() throws Exception {
        // 먼저 한번 가입시킨다.
        String signupJson = createSignupJson("test_passenger", "passenger@test.com", "password123", "passenger");
        mockMvc.perform(post("/api/auth/signup/passenger")
            .contentType(MediaType.APPLICATION_JSON)
            .content(signupJson));

        // 같은 이메일로 다시 가입 시도
        mockMvc.perform(post("/api/auth/signup/passenger")
                .contentType(MediaType.APPLICATION_JSON)
                .content(signupJson))
            .andExpect(status().isBadRequest())
            .andDo(print());
    }

    @Test
    @DisplayName("로그인 성공 및 토큰 발급")
    void login_Success() throws Exception {
        // 1. 테스트용 사용자 먼저 회원가입
        String signupJson = createSignupJson("login_user", "login@test.com", "password123", "passenger");
        mockMvc.perform(post("/api/auth/signup/passenger")
            .contentType(MediaType.APPLICATION_JSON)
            .content(signupJson));

        // 2. 로그인 시도
        String loginJson = "{\"name\":\"login_user\", \"password\":\"password123\"}";

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
            .andExpect(status().isOk()) // 200 OK 상태 코드 확인
            .andExpect(jsonPath("$.grantType").value("Bearer")) // grantType 필드 확인
            .andExpect(jsonPath("$.accessToken").exists()) // accessToken 존재 여부 확인
            .andExpect(cookie().exists("refreshToken")) // refreshToken 쿠키 존재 여부 확인
            .andDo(print());
    }

    @Test
    @DisplayName("유효한 토큰으로 보호된 API 접근 성공")
    void accessProtectedApi_Success() throws Exception {
        // 1. 회원가입
        String signupJson = createSignupJson("api_user", "api@test.com", "password123", "passenger");
        mockMvc.perform(post("/api/auth/signup/passenger")
            .contentType(MediaType.APPLICATION_JSON)
            .content(signupJson));

        // 2. 로그인하여 토큰 획득
        String loginJson = "{\"email\":\"api@test.com\", \"password\":\"password123\"}";
        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
            .andReturn();

        // 응답 본문에서 accessToken 추출
        String responseBody = loginResult.getResponse().getContentAsString();
        String accessToken = objectMapper.readTree(responseBody).get("accessToken").asText();

        // 3. 보호된 API에 토큰과 함께 요청
        // 예시로, '/api/some/protected/endpoint' 라는 보호된 엔드포인트가 있다고 가정합니다.
        // (실제로는 SecurityConfig에 따라 인증이 필요한 아무 경로)
        // 이 테스트를 위해서는 간단한 테스트용 컨트롤러를 추가해야 할 수 있습니다.
        // 예: @GetMapping("/api/test/me") public String getMyInfo(Authentication auth) { return auth.getName(); }

        // SecurityConfig에서 .anyRequest().authenticated()로 설정했으므로,
        // 존재하지 않는 경로라도 인증 필터를 통과하는지 테스트할 수 있습니다.
        mockMvc.perform(get("/api/any-protected-path")
                .header("Authorization", "Bearer " + accessToken))
            .andExpect(status().isNotFound()) // 경로가 없으므로 404 Not Found가 정상. 401/403이 아니면 인증 성공.
            .andDo(print());
    }

    @Test
    @DisplayName("유효하지 않은 토큰으로 보호된 API 접근 실패")
    void accessProtectedApi_Fail_InvalidToken() throws Exception {
        String invalidToken = "this-is-an-invalid-token";

        mockMvc.perform(get("/api/any-protected-path")
                .header("Authorization", "Bearer " + invalidToken))
            .andExpect(status().isUnauthorized()) // 401 Unauthorized 상태 코드 확인
            .andDo(print());
    }
}
