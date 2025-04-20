package org.example.expert.exception;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder

public class CustomExceptionReponseDTO {

    private String message;
    private String path;
    private LocalDateTime dateTime;

    public CustomExceptionReponseDTO(ExceptionCode exceptionCode, String path, LocalDateTime dateTime) {
        this.message = exceptionCode.getMessage();
        this.path = path;
        this.dateTime = LocalDateTime.now();
    }

}
