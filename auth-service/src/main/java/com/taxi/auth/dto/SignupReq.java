package com.taxi.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupReq {
    // 공통 필드
    private String name;
    private String email;
    private String password;
    private String phoneNumber;

    // 드라이버 전용 필드
    private String licenseNumber;
    private String vehicleNumber;
    private String vehicleModel;
    private String vehicleType;
}
