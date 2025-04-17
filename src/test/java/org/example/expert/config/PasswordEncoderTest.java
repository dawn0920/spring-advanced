package org.example.expert.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

// Mockito 확장 사용
@ExtendWith(SpringExtension.class)
class PasswordEncoderTest {

//    테스트하려는 클래스의 인스턴스를 생성 및 클래스의 필드에 @Mock로 만든 의존 객체를 자동으로 주입
//    @Mock -> 가짜 객체
    @InjectMocks
    private PasswordEncoder passwordEncoder;

    @Test
    void matches_메서드가_정상적으로_동작한다() {
        // given
        String rawPassword = "testPassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // when
//        boolean matches = passwordEncoder.matches(encodedPassword, rawPassword);
        // 실제 passwordEncoder에서는 public boolean matches(String rawPassword, String encodedPassword)
        // matches 가 위에와 같이 설정되어있음 -> 순서가 잘못됨
        boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);

        // then
        assertTrue(matches);
    }
}
