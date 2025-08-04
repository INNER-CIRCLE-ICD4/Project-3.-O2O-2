package com.taxi.location.api;

import com.taxi.location.dto.LocationDto;
import com.taxi.location.service.LocationService;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LocationController.class)
public class LocationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private LocationService locationService;

    @Test
    @DisplayName("드라이버 위치 업데이트 API 호출 테스트")
    public void updateDriverLocation() throws Exception {
        // given
        String driverId = "driver-123";
        LocationDto request = new LocationDto(driverId, 37.5665, 126.9780);

        // when & then
        mockMvc.perform(put("/api/drivers/{driverId}/location", driverId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());

        // verify
        verify(locationService).updateDriverLocation(driverId, request);
    }
}
