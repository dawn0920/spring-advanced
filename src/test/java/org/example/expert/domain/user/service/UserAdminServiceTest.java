package org.example.expert.domain.user.service;

import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.user.dto.request.UserRoleChangeRequest;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserAdminServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserAdminService userAdminService;

    @Test
    public void 역할_변경_요청_중_유저_없음_예외_발생() {
        // given
        long userId = 404L;
        User user = new User("test@test.com", "encodedPassword", UserRole.USER);
        UserRoleChangeRequest request = new UserRoleChangeRequest("ADMIN");

        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            userAdminService.changeUserRole(userId, request);
        });

        // then
        assertEquals(ExceptionCode.USER_NOT_FOUND, exception.getExceptionCode());
    }

    @Test
    public void 역할_변경_성공() {
        // given
        long userId = 404L;
        User user = new User("test@test.com", "encodedPassword", UserRole.USER);
        UserRoleChangeRequest request = new UserRoleChangeRequest("ADMIN");

        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        // when
        userAdminService.changeUserRole(userId, request);

        // then
        assertEquals(UserRole.ADMIN, user.getUserRole());
    }
}
