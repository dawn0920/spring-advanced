package org.example.expert.domain.todo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TodoSaveRequest {

    @NotBlank
    @Size(max = 50, message = "제목은 최대 50자까지 입력 가능합니다")
    private String title;

    @NotBlank
    @Size(max = 255, message = "내용은 최대 255자까지 입력 가능합니다")
    private String contents;
}
