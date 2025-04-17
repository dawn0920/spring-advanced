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
        // rawPassword.toCharArray() - 평문 비밀번호를 charp[]로 변환
        // BCrypt.verifyer().verify(···) - 검증 메서드
        // encodedPassword 복호화 하지 않고 -> rawPassword를 같은 조건으로 해싱 -> 두 해시 값이 같은지 반환
        // 복호화 (암호문을 평문으로 되돌림), 해싱(암호화된 문자열로 변환-복호화 불가능), 암호회(암호를 변환 후 복호화 가능)
        //
        // result.verified -> true(비밀번호 일치), false(비밀번호 일치X)
        BCrypt.Result result = BCrypt.verifyer().verify(rawPassword.toCharArray(), encodedPassword);
        return result.verified;
    }
}
