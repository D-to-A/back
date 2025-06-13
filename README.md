## 추가된 종속성

| 라이브러리 이름                   | 설명                                                                    |
| -------------------------- | --------------------------------------------------------------------- |
| **Spring Boot DevTools**   | 핫 리로딩 등 개발 편의 기능을 제공하는 도구 모음                                          |
| **Lombok**                 | `@Getter`, `@Setter`, `@Builder` 등 반복되는 코드를 줄여주는 어노테이션 기반 코드 자동 생성 도구 |
| **Spring Web**             | REST API 개발에 필수적인 웹 MVC 기능 (`@RestController`, `@RequestMapping` 등)   |
| **Spring Security**        | 인증/인가 처리를 위한 보안 프레임워크, JWT 인증 처리 등에서 활용                               |
| **JDBC API**               | Java에서 DB와 직접 통신할 수 있게 해주는 기본 API (`java.sql`)                        |
| **Spring Data JPA**        | JPA 기반 ORM 매핑을 위한 Spring 확장 라이브러리. Repository 인터페이스 자동 구현 지원          |
| **Spring Data JDBC**       | JPA 없이 간단한 SQL 중심의 JDBC 매핑을 제공하는 Spring Data 모듈                       |
| **MySQL Driver**           | MySQL 데이터베이스와 Java 애플리케이션을 연결해주는 드라이버 (`com.mysql:mysql-connector-j`) |
| **Validation**             | Bean Validation(JSR-380) 기반 유효성 검증 (`@Valid`, `@NotNull`, `@Email` 등) |
| **CycloneDX SBOM support** | 프로젝트의 소프트웨어 구성 요소(BOM)를 생성하는 보안 도구로, 취약점 분석 등에 활용 가능                  |

---

## 구현된 기능

### 인증/보안

| 메서드 | URI | 설명 |
| --- | --- | --- |
| POST | `/auth/register` | 회원가입 |
| POST | `/auth/login` | 로그인 (JWT 발급) |
| POST | `/auth/logout` | 로그아웃 (refreshToken 초기화 예정) |

---

### 마이페이지

| 메서드    | URI                                | 설명              |
| ------ | ---------------------------------- | --------------- |
| GET    | `/users/{userId}/awards`           | 특정 유저의 수상 내역 조회 |
| POST   | `/users/{userId}/awards`           | 수상 내역 등록     |
| DELETE | `/users/{userId}/awards/{awardId}` | 특정 유저 수상 내역 삭제     |


| 메서드 | URI | 설명 |
| --- | --- | --- |
| GET | `/users/{userId}` | 유저 정보 조회 |
| PATCH | `/users/{userId}/club` | 유저 소속 동아리 변경 |
| PATCH | `/users/{userId}/password` | 비밀번호 변경 |
| PATCH | `/users/{userId}/info` | 유저 정보 수정 |

---

### 동아리

| 메서드 | URI | 설명 |
| --- | --- | --- |
| GET | `/club/all` | 전체 동아리 목록 조회 |
| POST | `/club/create` | 동아리 생성 (임시 API) |
| DELETE | `/club/{clubId}` | 동아리 삭제 |

---

### 🧪 테스트용 프론트 HTML

- `/index.html` 로컬 테스트 HTML 제공
- fetch API 기반으로 회원가입 / 유저 리스트 / 삭제 테스트 가능
- **동아리 리스트를 이름 기반으로 선택 가능**

---

## JWT 인증 방식

- AccessToken, RefreshToken 모두 발급
- HTTP 요청 시 `Authorization: Bearer {accessToken}` 헤더 필수
- 인증된 사용자만 마이페이지 접근 가능

---

## DB 테이블 요약

### users 테이블

| 필드 | 설명 |
| --- | --- |
| id | BIGINT, AUTO_INCREMENT |
| username | 아이디 |
| password | 비밀번호 (BCrypt 암호화) |
| real_name | 실제 이름 |
| grade / class_name / student_num | 학년 / 반 / 번호 |
| club_id | 소속 동아리 (FK) |
| main_stack / sub_stack | 기술 스택 |
| refresh_token | 리프레시 토큰 저장 |
| created_at / updated_at | 생성/수정 시간 |

### clubs 테이블
| 필드            | 설명                               |
| ------------- | -------------------------------- |
| `id`          | `BIGINT`, `AUTO_INCREMENT`, 기본 키 |
| `name`        | 동아리 이름                           |
| `club_stack`  | 동아리의 주요 기술 스택                    |
| `description` | 동아리 설명/소개                        |


### awards 테이블
| 필드            | 설명                               |
| ------------- | -------------------------------- |
| `id`          | `BIGINT`, `AUTO_INCREMENT`, 기본 키 |
| `title`       | 수상 제목                            |
| `description` | 수상 설명                            |
| `awarded_at`  | 수상 일자 (`DATE`)                   |
| `user_id`     | 수상자(`users` 테이블) 외래 키 (`BIGINT`) |

---

## 참고

- `JdbcTemplate`을 활용하여 `AUTO_INCREMENT` 상태를 수시로 조정
- 유저 삭제 시 자동으로 `AUTO_INCREMENT` 정렬
- 서버 시작 시 `users`, `clubs` 테이블의 현재 `MAX(id)` 기반으로 초기화
