package com.taxi.rideUp.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taxi.rideUp.controller.ScoreHistoryController;
import com.taxi.rideUp.domain.ScoreHistoryEntity;
import com.taxi.rideUp.dto.request.ScoreHistoryCreateRequest;
import com.taxi.rideUp.dto.response.external.DriverValidationResponse;
import com.taxi.rideUp.exception.external.DriveManageValidationException;
import com.taxi.rideUp.service.ScoreHistoryService;
import com.taxi.rideUp.unit.fixture.ScoreHistoryFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * packageName : com.taxi.rideUp.unit.controller
 * fileName    : ScoreHistoryControllerTest
 * author      : ckr
 * date        : 25. 8. 2.
 * description : ScoreHistoryController 단위 테스트
 */
@WebMvcTest(ScoreHistoryController.class)
@DisplayName("ScoreHistoryController 단위 테스트")
class ScoreHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ScoreHistoryService scoreHistoryService;

    @Test
    @DisplayName("평점 생성 API를 성공적으로 호출한다")
    void createScoreHistory_Success() throws Exception {
        // given
        Long driveManageId = 100L;
        ScoreHistoryCreateRequest request = ScoreHistoryFixture.createRequest(2);
        ScoreHistoryEntity savedEntity = ScoreHistoryFixture.createEntity(1L, driveManageId, 2);

        given(scoreHistoryService.createScoreHistory(eq(driveManageId), any(ScoreHistoryCreateRequest.class)))
            .willReturn(savedEntity);

        // when & then
        mockMvc.perform(post("/drive-manages/{driveManageId}/score-histories", driveManageId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.scoreHistoryId").value(1L))
            .andExpect(jsonPath("$.driveManageId").value(driveManageId))
            .andExpect(jsonPath("$.score").value(2));
    }

    @Test
    @DisplayName("평점 생성 API - 유효하지 않은 평점 범위로 요청 시 400 에러를 반환한다")
    void createScoreHistory_InvalidScoreRange() throws Exception {
        // given
        Long driveManageId = 100L;
        ScoreHistoryCreateRequest request = ScoreHistoryFixture.createRequest(5); // 유효 범위 초과

        // when & then
        mockMvc.perform(post("/drive-manages/{driveManageId}/score-histories", driveManageId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("평점 생성 API - null 평점으로 요청 시 400 에러를 반환한다")
    void createScoreHistory_NullScore() throws Exception {
        // given
        Long driveManageId = 100L;
        String invalidRequestJson = "{\"score\": null}";

        // when & then
        mockMvc.perform(post("/drive-manages/{driveManageId}/score-histories", driveManageId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidRequestJson))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("평점 생성 API - DriveManageId 검증 실패 시 400 에러를 반환한다")
    void createScoreHistory_DriveManageValidationFails() throws Exception {
        // given
        Long driveManageId = 100L;
        ScoreHistoryCreateRequest request = ScoreHistoryFixture.createRequest(2);

        ResponseEntity<DriverValidationResponse> badResponse = ResponseEntity.badRequest().build();
        DriveManageValidationException exception = new DriveManageValidationException(badResponse);

        willThrow(exception)
            .given(scoreHistoryService)
            .createScoreHistory(eq(driveManageId), any(ScoreHistoryCreateRequest.class));

        // when & then
        mockMvc.perform(post("/drive-manages/{driveManageId}/score-histories", driveManageId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("평점 생성 API - 음수 평점으로 성공적으로 생성한다")
    void createScoreHistory_NegativeScore() throws Exception {
        // given
        Long driveManageId = 100L;
        ScoreHistoryCreateRequest request = ScoreHistoryFixture.createRequest(-1);
        ScoreHistoryEntity savedEntity = ScoreHistoryFixture.createEntity(1L, driveManageId, -1);

        given(scoreHistoryService.createScoreHistory(eq(driveManageId), any(ScoreHistoryCreateRequest.class)))
            .willReturn(savedEntity);

        // when & then
        mockMvc.perform(post("/drive-manages/{driveManageId}/score-histories", driveManageId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.scoreHistoryId").value(1L))
            .andExpect(jsonPath("$.driveManageId").value(driveManageId))
            .andExpect(jsonPath("$.score").value(-1));
    }
}
