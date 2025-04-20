package org.example.expert.domain.user.service;

import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.example.expert.exception.CustomException;
import org.example.expert.exception.ExceptionCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UserResponse getUser(long userId) {
//        User user = userRepository.findById(userId).orElseThrow(() -> new InvalidRequestException("User not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_FOUND));
        return new UserResponse(user.getId(), user.getEmail());
    }

    @Transactional
    public void changePassword(long userId, UserChangePasswordRequest userChangePasswordRequest) {
//        3. 코드 개선 퀴즈 - Validation (검증 - 입력값이 올바른지)
//        패키지 package org.example.expert.domain.user.service; 의 UserService 클래스에 있는
//        changePassword() 중 아래 코드 부분을 해당 API의 요청 DTO에서 처리할 수 있게 개선해주세요.
//        'org.springframework.boot:spring-boot-starter-validation' 라이브러리를 활용해주세요!
//        @NotNull, @NotBlank, @Email, @Size 같은 애너테이션 포함
//        if (userChangePasswordRequest.getNewPassword().length() < 8 ||
//                !userChangePasswordRequest.getNewPassword().matches(".*\\d.*") ||
//                !userChangePasswordRequest.getNewPassword().matches(".*[A-Z].*")) {
//            throw new InvalidRequestException("새 비밀번호는 8자 이상이어야 하고, 숫자와 대문자를 포함해야 합니다.");
//        }

        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new InvalidRequestException("User not found"));
                .orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_FOUND));


        if (passwordEncoder.matches(userChangePasswordRequest.getNewPassword(), user.getPassword())) {
//            throw new InvalidRequestException("새 비밀번호는 기존 비밀번호와 같을 수 없습니다.");
            throw new CustomException(ExceptionCode.DUPLICATE_PASSWORD);
        }

        if (!passwordEncoder.matches(userChangePasswordRequest.getOldPassword(), user.getPassword())) {
//            throw new InvalidRequestException("잘못된 비밀번호입니다.");
            throw new CustomException(ExceptionCode.PASSWORD_MISMATCH);
        }

        user.changePassword(passwordEncoder.encode(userChangePasswordRequest.getNewPassword()));
    }
}
