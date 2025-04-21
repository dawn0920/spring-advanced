package org.example.expert.domain.auth.service;

import org.example.expert.config.JwtUtil;
import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.auth.dto.request.SigninRequest;
import org.example.expert.domain.auth.dto.request.SignupRequest;
import org.example.expert.domain.auth.dto.response.SigninResponse;
import org.example.expert.domain.auth.dto.response.SignupResponse;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    // AuthService가 의존하고 있는 실제 클래스들을 가짜 객체로 대체
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    // @Mock로 만들어둔 의존성을 자동으로 주입
    @InjectMocks
    private AuthService authService;

    @Test
    public void 회원가입_시_이미_존재하는_이메일이면_에러를_반환() {
        // given
        SignupRequest request = new SignupRequest("test@test.com", "password1", "USER");

        // anyString() -> 어떤 문자열이 들어와도 상관 X
        given(userRepository.existsByEmail(anyString())).willReturn(true);

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            authService.signup(request);
        });

        // then
        assertEquals(ExceptionCode.EMAIL_ALREADY_EXISTS, exception.getExceptionCode());
    }

    @Test
    public void 회원가입을_정상적으로_진행() {
        // given
        SignupRequest request = new SignupRequest("test@test.com", "password", "USER");

        given(userRepository.existsByEmail(anyString())).willReturn(false);
        // 패턴 검증은 보통 Controller나 DTO 레벨에서 일어나기 때문에 Service만 테스트 중이라  encodedPassword 라고 작성해도 에러가 발생하지 않음
        given(passwordEncoder.encode(anyString())).willReturn("encodedPassword");
        given(userRepository.save(any())).willReturn(new User("test@test.com", "encodedPassword", UserRole.USER));
        given(jwtUtil.createToken(any(), anyString(), any())).willReturn("mocked-token");

        // when
        SignupResponse response = authService.signup(request);

        // then
        assertNotNull(response);
        assertEquals("mocked-token", response.getBearerToken());
    }

    @Test
    public void 로그인_시_가입하지_않은_이메일이면_에러_반환() {
        // given
        SigninRequest request = new SigninRequest("test@test.com", "password");

        // anyString() -> 어떤 문자열이 들어와도 상관 X
        // Optional.empty() -> 항상 비어있는 객체 반환
        given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            authService.signin(request);
        });

        // then
        assertEquals(ExceptionCode.ACCOUNT_NOT_FOUND, exception.getExceptionCode());
    }

    @Test
    public void 로그인_시_비밀번호가_일치하지_않으면_에러_반환() {
        // given
        SigninRequest request = new SigninRequest("test@test.com", "wrongPassword");
        User user = new User("test@test.com", "encodedPassword", UserRole.USER);

        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));
        // passwordEncoder.matches(CharSequence rawPassword, String encodedPassword)
        // matches 는 rawPassword(평문 비밀번호), encodedPassword(인코딩 비밀번호) 두개를 비교함
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            authService.signin(request);
        });

        // then
        assertEquals(ExceptionCode.PASSWORD_MISMATCH, exception.getExceptionCode());
    }

    @Test
    public void 로그인을_정상적으로_진행() {
        // given
        SigninRequest request = new SigninRequest("test@test.com", "wrongPassword");
        User user = new User("test@test.com", "encodedPassword", UserRole.USER);

        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
        given(jwtUtil.createToken(any(), anyString(), any())).willReturn("mocked-token");

        // when
        SigninResponse response = authService.signin(request);

        // then
        assertNotNull(response);
        assertEquals("mocked-token", response.getBearerToken());
    }


}
