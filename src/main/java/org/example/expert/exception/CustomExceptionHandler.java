package org.example.expert.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class CustomExceptionHandler {

    // CustomException 이 발생했을때 실행 될 메서드
    @ExceptionHandler(CustomException.class)
    // CustomException - 파라미터, HttpServletRequest - 요청 정보
    public ResponseEntity<CustomExceptionReponseDTO> handleCustomException(CustomException e, HttpServletRequest request) {
        // CustomException  안에 있는 enum값을 꺼냄
        ErrorCode errorCode = e.getExceptionCode();

        // HttpStatus 기반으로 응답 코드 설정
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(new CustomExceptionReponseDTO(
                        errorCode.getMessage(),
                        request.getRequestURI(),
                        LocalDateTime.now()
                ));
    }

    // Valid, @NotBlank, @Email, @Pattern 등에서 유효성 검사를 통과하지 못할 경우 발생하는 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomExceptionReponseDTO> handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
        // 유효성 검사에서 실패한 필드의 에러 메시지를 가져옴(없으면 기본 메시지 사용)
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .findFirst()
                .orElse("유효성 검사에 실패했습니다.");

        // HTTP 상태 코드 400 (Bad Request)로 응답을 설정
        return ResponseEntity.badRequest()
                .body(new CustomExceptionReponseDTO(
                        errorMessage,
                        request.getRequestURI(),
                        LocalDateTime.now()
                ));
    }

}
