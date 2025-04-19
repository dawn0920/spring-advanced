package org.example.expert.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.example.expert.domain.user.dto.request.UserRoleChangeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;


@Aspect
@Component
public class AdminApiLoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(AdminApiLoggingAspect.class);

    public AdminApiLoggingAspect(ObjectMapper objectMapper) {
    }

    // @Pointcut - 어떤 메서드에 AOP를 적용할 것인지 정의 (타겟 지정)
    @Pointcut("execution(* org.example.expert.domain.comment.controller.CommentAdminController.deleteComment(..)) ")
    public void commentControllerMethod() {}

    @Pointcut("execution(* org.example.expert.domain.user.controller.UserAdminController.changeUserRole(..))")
    public void userControllerMethod() {}

    // @Before - 메서드 실행 전
    // @After - 메서드 실행 후
    // @AfterReturning - 메서드가 정상적으로 실행된 후
    // @AfterThrowing - 메서드 실행 중 예외가 발생했을때
    // @Around - 메서드 실행 전/후 모두 실행 (실행 제어)
    @Around("commentControllerMethod() || userControllerMethod()")
    // ProceedingJoinPoint - 현재 실행중인 joinpoint(타겟 메서드) 에 대한 정보 제공 및 직접 실행할 수 있게 하는 객체

    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {

        // 1. 요청한 사용자의 ID
        // RequestContextHolder.getRequestAttributes() - 쓰레드에 바인딩된 요청 정보를 관리하는 객체를 가져옴
        // 쓰레드 - 요청마다 따로 처리되는 것 / 바인딩 - 그 요청을 연결함
        // (ServletRequestAttributes) - 서블릿 기반의 요청 정보로 다운캐스팅
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String userId = request.getHeader("UserId");

        // 2. API 요청 시각
        String time = LocalDateTime.now().toString();

        // 3. API 요청 URL
        String uri = request.getRequestURI().toString();

        // 4.요청 본문(RequestBody)
        // ObjectMapper -> java를 json으로 바꾸거나 json을 java로 바꿈
        ObjectMapper objectMapper = new ObjectMapper();

        // joinPoint.getArgs(); -> 메서드에 전달된 모든 인자를 불러옴
        Object[] args = joinPoint.getArgs();
        Object requestBody = null;

        for (Object arg : args) {
            // instanceof - 자바 객체 타입 체크
            // UserRoleChangeRequest 타입이 있으면 그걸 JSON 문자열로 바꿔서 저장해라
            if (arg instanceof UserRoleChangeRequest) {
                requestBody = objectMapper.writer().writeValueAsString(arg);
            }
        }

        // 메서드 실행
        Object result = joinPoint.proceed();

        // 5. 응답 본문(ResponseBody)
        String responseBody = objectMapper.writeValueAsString(result);

        log.info("요청한 사용자의 ID : {}\n " +
                "API 요청 시각 : {}\n" +
                "API 요청 URL : {}\n" +
                "요청 본문(RequestBody) : {}\n" +
                "응답 본문(ResponseBody) : {}\n", userId, time, uri, requestBody, responseBody);

        // .getRequest() - 서블릿 요청 객체인 HttpServletRequest를 가지고 옴 (URI, 헤더, 메서드 타입 등을 알 수 있음)

        return result;
    }
}
