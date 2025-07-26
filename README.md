# 택시 매칭 서비스 (MSA with Spring Boot & Java)

Spring Boot와 Gradle을 사용하여 마이크로서비스 아키텍처(MSA) 기반의 택시 매칭 서비스를 구현한 **순수 Java** 멀티 모듈 프로젝트입니다.

## 🚀 아키텍처
- **MSA (Microservices Architecture):** 각 도메인(회원, 위치, 매칭 등)을 독립적인 서비스로 분리하여 개발 및 배포의 유연성을 확보합니다.
- **Multi-Module (Monorepo):** 모든 마이크로서비스 코드를 단일 Gradle 프로젝트 내의 하위 모듈로 관리하여 코드의 일관성과 의존성 관리를 용이하게 합니다.
- **기술 스택:** Java 21, Spring Boot 3.x, Gradle, JPA(Hibernate), H2 (예시)

## 📦 모듈 구조

- **/auth-service**: 회원 가입, 로그인, 인증 등 사용자 관련 기능을 담당합니다.
- **/location-service**: 승객 및 드라이버의 위치 데이터를 수집하고 주변 드라이버를 검색하는 기능을 담당합니다.
- **/matching-service**: 승객의 호출 요청에 대해 최적의 드라이버를 매칭하는 핵심 로직을 담당합니다.
- **/rating-service**: 운행 종료 후 상호 평가를 기록하고 관리합니다.
- **/notification-service**: 매칭, 운행 상태 등 각종 알림을 발송합니다.
- **/admin-service**: 관리자용 기능을 제공합니다. (드라이버 승인 등)

## 🛠️ 시작하기

### 사전 요구사항
- Java 21 (or later)
- Gradle 8.x
