package org.example.expert.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.config.JwtUtil;
import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.auth.dto.request.SigninRequest;
import org.example.expert.domain.auth.dto.request.SignupRequest;
import org.example.expert.domain.auth.dto.response.SigninResponse;
import org.example.expert.domain.auth.dto.response.SignupResponse;
import org.example.expert.domain.auth.exception.AuthException;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public SignupResponse signup(SignupRequest signupRequest) {

//        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());

        UserRole userRole = UserRole.of(signupRequest.getUserRole());

        // Early Return
        // 조건을 만족하면 함수나 메서드에서 일찍 빠져나옴

        // 리팩토링해서 해당 에러가 발생하는 상황일 때,
        // passwordEncoder의 encode() 동작이 불필요하게 일어나지 않게 코드를 개선
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
//             String  bearerToken = throw new InvalidRequestException("이미 존재하는 이메일입니다.");
//             예외는 String로 받을 수 있음 하지만 위의 형식으로는 되지 않음
//             try {
//                throw new IllegalArgumentException("에러 발생!");
//            } catch (IllegalArgumentException e) {
//                String str = e.getMessage(); // 예외 메시지를 문자열로 꺼냄
//                System.out.println("예외 메시지: " + str);
//            }
//             ❌ 이렇게는 안 됨
//            try {
//                throw new RuntimeException("에러 발생");
//            } catch (String str) { // 컴파일 에러 발생
//                System.out.println(str);
//            }
//             return new SignupResponse(bearerToken);
//             일반적으로 throw로 예외를 던지면 그 아래에 있는 값들은 모두 실행이 되지 않음
//             그렇기 때문에 코드를 아래로 바꾸면 불필요하게 일어나지 않음
            throw new InvalidRequestException("이미 존재하는 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());

        User newUser = new User(
                signupRequest.getEmail(),
                encodedPassword,
                userRole
        );
        User savedUser = userRepository.save(newUser);

        String bearerToken = jwtUtil.createToken(savedUser.getId(), savedUser.getEmail(), userRole);

        return new SignupResponse(bearerToken);
    }

    @Transactional(readOnly = true)
    public SigninResponse signin(SigninRequest signinRequest) {
        User user = userRepository.findByEmail(signinRequest.getEmail()).orElseThrow(
                () -> new InvalidRequestException("가입되지 않은 유저입니다."));

        // 로그인 시 이메일과 비밀번호가 일치하지 않을 경우 401을 반환합니다.
        if (!passwordEncoder.matches(signinRequest.getPassword(), user.getPassword())) {
            throw new AuthException("잘못된 비밀번호입니다.");
        }

        String bearerToken = jwtUtil.createToken(user.getId(), user.getEmail(), user.getUserRole());

        return new SigninResponse(bearerToken);
    }
}
