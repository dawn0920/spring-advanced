package org.example.expert.domain.user.service;

import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.auth.dto.request.SignupRequest;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.example.expert.exception.CustomException;
import org.example.expert.exception.ExceptionCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    public void 존재하지_않는_유저_조회시_예외_발생() {
        // given
        long userId = 404L;

        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.getUser(userId);
        });

        // then
        assertEquals(ExceptionCode.USER_NOT_FOUND, exception.getExceptionCode());
    }

    @Test
    public void 존재하는_유저_조회_성공() {
        // given
        long userId = 1L;
        User user = new User("test@test.com", "encodedPassword", UserRole.USER);

        user.setId(userId);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        // when
        UserResponse result = userService.getUser(userId);

        // then
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("test@test.com", result.getEmail());
    }

    @Test
    public void 존재하지_않는_유저의_비밀번호_변경_시_에러_발생() {
        // given
        long userId = 404L;
        UserChangePasswordRequest request = new UserChangePasswordRequest("oldPassword", "newPassword");

        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.changePassword(userId, request);
        });

        // then
        assertEquals(ExceptionCode.USER_NOT_FOUND, exception.getExceptionCode());
    }

    @Test
    public void 기존_비밀번호와_같은_비밀번호로_변경_시_에러_발생() {
        // given
        long userId = 1L;
        User user = new User("test@test.com", "encodedPassword", UserRole.USER);
        UserChangePasswordRequest request = new UserChangePasswordRequest("oldPassword", "oldPassword");

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.changePassword(userId, request);
        });

        // then
        assertEquals(ExceptionCode.DUPLICATE_PASSWORD, exception.getExceptionCode());
    }

    @Test
    public void 기존_비밀번호_불일치_시_에러_발생() {
        // given
        long userId = 1L;
        User user = new User("test@test.com", "encodedPassword", UserRole.USER);
        UserChangePasswordRequest request = new UserChangePasswordRequest("oldPassword", "newPassword");

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.changePassword(userId, request);
        });

        // then
        assertEquals(ExceptionCode.PASSWORD_MISMATCH, exception.getExceptionCode());
    }

    @Test
    public void 비밀번호_변경_성공() {
        // given
        long userId = 1L;
        User user = new User("test@test.com", "encodedOldPassword", UserRole.USER);
        UserChangePasswordRequest request = new UserChangePasswordRequest("oldPassword", "newPassword");

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(passwordEncoder.matches("oldPassword", user.getPassword())).willReturn(true);
        given(passwordEncoder.matches("newPassword", user.getPassword())).willReturn(false);
        given(passwordEncoder.encode("newPassword")).willReturn("encodedNewPassword");

        // when
        userService.changePassword(userId, request);

        // then
        assertEquals("encodedNewPassword", user.getPassword());
    }



}
