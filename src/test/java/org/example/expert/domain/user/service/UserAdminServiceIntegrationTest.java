package org.example.expert.domain.user.service;

import jakarta.transaction.Transactional;
import org.example.expert.domain.user.dto.request.UserRoleChangeRequest;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional // 테스트가 끝나면 자동으로 롤백
public class UserAdminServiceIntegrationTest {

    @Autowired
    private UserAdminService userAdminService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll(); // 테스트 실행 전 유저 데이터 비우기
        userRepository.flush();  // 즉시 DB에 반영
//        System.out.println("User count after deleteAll: " + userRepository.count());
    }

    @Test
    void 역할_변경_성공_테스트() {
        // given
        User user = new User("test@test.com", "encodePassword", UserRole.USER);
        userRepository.save(user);

        UserRoleChangeRequest request = new UserRoleChangeRequest("ADMIN");

        // when
        userAdminService.changeUserRole(user.getId(), request);

        // then
        User changed = userRepository.findById(user.getId()).get();
        assertEquals(UserRole.ADMIN, changed.getUserRole());
        System.out.println("Changed user role: " + changed.getUserRole());
    }
}
