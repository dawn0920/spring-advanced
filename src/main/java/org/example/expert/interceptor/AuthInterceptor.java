package org.example.expert.interceptor;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.expert.config.JwtUtil;
import org.example.expert.domain.user.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

// Component 의존성 주입 기능 - Spring IoC 컨테이너에서 관리하게 만드는 역할
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final UserService userService;

    public AuthInterceptor(UserService userService) {
        this.userService = userService;
    }

//    1. **Interceptor**를 사용하여 구현하기
//    - 요청 정보(`HttpServletRequest`)를 사전 처리합니다.
//    - 인증 성공 시, 요청 시각과 URL을 로깅하도록 구현하세요.
//    Interceptor - HTTP 요청이 컨트롤러에 도달하기 전/후에 가로채서 처리할 수 있는 컴포넌트

//    가로챈다 -
//    클라이언트 요청 -> Interceptor(preHandle) 가로챔 ->
//    컨트롤러 ->  Interceptor(postHandle) 가로챔 ->
//    응답 렌더링 -> Interceptor(afterCompletion) 가로챔 -> 클라이언트

//     요청 정보(HttpServletRequest)를 사전 처리합니다.
//     인증 성공 시, 요청 시각과 URL을 로깅하도록 구현
//    - 어드민 인증 여부를 확인합니다.
//    - 인증되지 않은 경우 예외를 발생시킵니다.


    @Override
    // preHandle의 경우 요청을 계속 진행할지 말지 결정하기 때문에 반환이 boolean 형식이다.
    public boolean preHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler) throws Exception {
        // 클라이언트에서 HTTP 요청을 보낼 때 Header에 UserRole 라는 값을 보냈을때 그 안에 있는 값을 변수에 저장
        String userRole = request.getHeader("UserRole");

        // setStatus() -> 응답 코드를 설정하는 메서드
        // SC_UNAUTHORIZED -> 인증이 안됨 (상태코드 401 -> 로그인 필요)
        // SC_FORBIDDEN -> 인증은 되었지만 권한이 없음 (상태코드 403)
        // 두가지는 다른 상태 코드를 반환하기 때문에 코드를 두가지로 나눠야 함.

        if (userRole == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 상태 코드 401 (로그인 필요)
            response.getWriter().write("로그인이 필요.");
            return false;
        }

        if (!"ADMIN".equals(userRole)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 상태 코드 403 (권한 없음)
            response.getWriter().write("접근 권한 없음."); // 에러 메세지 응답
            return false; // 컨트롤러 실행 X
        }

        System.out.println("[ADMIN LOG]" +
                    "\n[Local Time]" + LocalDateTime.now() +
                    "\n[Request URI]" + request.getMethod() + " " + request.getRequestURI());
        // 해당문구를 log 형식으로 출력하는 방식에 대해서 생각해볼것

        return true; // 통과
    }

//    @Override
//    public void afterCompletion(HttpServletRequest request,
//                                HttpServletResponse response,
//                                Object object,
//                                Exception exception) {
//        // Exception 같은 경우 코드 실행 중 발생했던 예외를 알려줌 (없으면 null)
//        // getStatus() == HttpServletResponse.SC_OK -> 제대로 코드가 실행되었을때 (200이 작동할 경우 수행)
//        // LocalDateTime.now() - 현재 시간
//        // request.getMethod() - HTTP request 메서드(GET, POST 등)
//        // request.getRequestURI() - request uri 정보
//        if (response.getStatus() == HttpServletResponse.SC_OK) {
//            System.out.println("[ADMIN LOG]" + LocalDateTime.now() +
//                    " - " + request.getMethod() + " " + request.getRequestURI());
//        }
//    }

//    Interceptor와 AOP를 활용한 API 로깅 - 요청혹은 응답이 나갈 때 로그를 자동으로 기록
//    API 로깅 - 매서드 실행 전/후/예외
//    @Aspect + @Around 등 사용
//     비즈니스 로직 레벨에서 미세 조정 가능 / 메서드 호출 레벨에서 세부 로직 감시
//    Interceptor - HTTP 요청 전체
//    HandlerInterceptor 인터페이스 구현
//    요청 흐름을 한눈에 파악 가능 // HTTP 레벨에서 전체 흐름 감시

}
