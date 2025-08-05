INSERT INTO notification_type (type, description, title, message)
VALUES
-- auth-service
('DRIVER_SIGNUP_APPROVED', '드라이버 - 가입승인완료', '가입 승인 완료', '드라이버님의 가입이 승인되었습니다.'),
('DRIVER_SIGNUP_REJECTED', '드라이버 - 가입승인거절', '가입 승인 거절', '드라이버님의 가입이 거절되었습니다.'),

-- matching-service
('DRIVER_CALL_CANCELLED', '드라이버 - 호출취소', '호출 취소', '승객이 호출을 취소했습니다.'),
('DRIVER_MATCHED', '드라이버 - 매칭완료', '매칭 완료', '승객과 매칭이 완료되었습니다.'),
('DRIVER_RIDE_STARTED', '드라이버 - 운행시작', '운행 시작', '운행이 시작되었습니다.'),
('DRIVER_NEAR_DESTINATION', '드라이버 - 도착지점 거의도착', '목적지 인근 도착', '목적지 인근에 도착했습니다.'),
('DRIVER_RIDE_COMPLETED', '드라이버 - 운행완료', '운행 완료', '운행이 완료되었습니다.'),

('PASSENGER_MATCHED', '승객 - 매칭완료', '매칭 완료', '드라이버와 매칭이 완료되었습니다.'),
('PASSENGER_RIDE_STARTED', '승객 - 운행시작', '운행 시작', '운행이 시작되었습니다.'),
('PASSENGER_NEAR_DESTINATION', '승객 - 도착지점 거의도착', '목적지 인근 도착', '목적지 인근에 도착했습니다.'),
('PASSENGER_RIDE_COMPLETED', '승객 - 운행완료', '운행 완료', '운행이 완료되었습니다.');
