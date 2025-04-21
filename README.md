# Introduction
[Spring 6기] Spring 심화 주차 개인 과제

🔗 https://github.com/dawn0920/spring-advanced

## 📝 프로젝트 소개

### 개발 기간
2025.04.17 ~ 2025.04.21


## 필수기능
### Lv 1
  코드 개선
  
  [✔️]  1. 코드 개선 퀴즈 - Early Return

  [✔️]  2. 리팩토링 퀴즈 - 불필요한 if-else 피하기

  [✔️]  3. 코드 개선 퀴즈 - Validation
  
---

### Lv 2
  N+1 문제
  
  [✔️]  N+1 문제
  
    - TodoController와 TodoService를 통해 Todo 관련 데이터를 처리
    
    - 여기서 N+1 문제가 발생할 수 있는 시나리오는 getTodos 메서드에서 모든 Todo를 조회할 때, 각 Todo와 연관된 엔티티를 개별적으로 가져오는 경우.

    - JPQL 특정 기능을 사용하여 N+1 문제를 해결하고 있는 TodoRepository가 존재 -> 이 기능을 활용해 N+1을 해결하고 있는지 분석

    - 이를 동일한 동작을 하는 @EntityGraph 기반의 구현으로 수정
  
---

### Lv 3
  테스트코드 연습
  
  [✔️]  1. 테스트 코드 연습 - 1 (예상대로 성공하는지에 대한 케이스입니다.)

  [✔️]  2. 테스트 코드 연습 - 2 (예상대로 예외처리 하는지에 대한 케이스입니다.)
  
    [✔️] 1번 케이스
    
    [✔️] 2번 케이스

    [✔️] 2번 케이스

---

## 도전기능
### Lv 4
  API 로깅
  
  [✔️]  Interceptor 
  
    - 요청 정보(HttpServletRequest)를 사전 처리
    
    - 인증 성공 시, 요청 시각과 URL을 로깅

    - 어드민 인증 여부를 확인

  [✔️]  AOP
  
    - 어드민 API 메서드 실행 전후에 요청/응답 데이터를 로깅
    
    - 로깅 내용에 아래 정보가 포함
      - 요청한 사용자의 ID
      - API 요청 시각
      - API 요청 URL
      - 요청 본문(`RequestBody`)
      - 응답 본문(`ResponseBody`)

    - @Around 어노테이션을 사용하여 어드민 API 메서드 실행 전후에 요청/응답 데이터를 로깅

    - 로깅은 Logger 클래스를 활용하여 기록
  
---

### Lv 5
  위 제시된 기능 이외 ‘내’가 정의한 문제와 해결 과정 (https://dawns2.tistory.com/145)
  
  [✔️]  1. 유효성 검증 개선

  [✔️]  2. 어노테이션 메시지 추가
  
  [✔️]  3. 예외 처리 변경
  
---

### Lv 6
  테스트 커버리지
  
  [✔️]  1. AuthServiceTest

  [✔️]  2. UserServiceTest
  
  [✔️]  3. UserAdminServiceTest
    



![Image](https://github.com/user-attachments/assets/ed50f349-3ece-421e-9219-f12d9d5fc7f0)

