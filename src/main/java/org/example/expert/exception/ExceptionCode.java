package org.example.expert.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode implements ErrorCode{
    // auth - 회원가입
    EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),

    // auth - 로그인
    ACCOUNT_NOT_FOUND(HttpStatus.UNAUTHORIZED, "가입되지 않은 유저입니다."),
    PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED, "잘봇된 비밀번호입니다."),

    // user
    USER_NOT_FOUND(HttpStatus.UNAUTHORIZED, "해당 유저를 찾을 수 없습니다."),
    DUPLICATE_PASSWORD(HttpStatus.BAD_REQUEST, "새 비밀번호는 기존 비밀번호와 같을 수 없습니다."),

    // todo
    TODO_NOT_FOUND(HttpStatus.NOT_FOUND, "Todo 항목을 찾을 수 없습니다."),

    // comment

    // manager
    MANAGER_NOT_FOUND(HttpStatus.NOT_FOUND, "매니저를 찾을 수 없습니다.");


    private final HttpStatus httpStatus;
    private final String errorMessage;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }
}
