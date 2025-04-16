package org.example.expert.config;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoder {

    public String encode(String rawPassword) {
        // rawPassword - 사용자가 입력한 비밀번호
        // .toCharArray - 문자 배열로 변환 (char[])
        // BCrypt.MIN_COST - 해시 반복 횟수 최소값 (COST - 계산에 걸리는 복잡도와 연산횟루를 조절)
        // hashToString() - cost값과 비밀번호로 해시 문자열 생성
        // BCrypt.withDefaults() - 기본 설정을 사용하는 BCrypt 인스턴스
        // 이렇게 코드를 해시하면 db를 탈취하더라도 비밀번호 원문은 알 수 없음 (salt(무작위값) 을 자동으로 붙임)
        return BCrypt.withDefaults().hashToString(BCrypt.MIN_COST, rawPassword.toCharArray());
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        BCrypt.Result result = BCrypt.verifyer().verify(rawPassword.toCharArray(), encodedPassword);
        return result.verified;
    }
}
