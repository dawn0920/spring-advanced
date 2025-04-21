package org.example.expert.domain.manager.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.manager.dto.request.ManagerSaveRequest;
import org.example.expert.domain.manager.dto.response.ManagerResponse;
import org.example.expert.domain.manager.dto.response.ManagerSaveResponse;
import org.example.expert.domain.manager.entity.Manager;
import org.example.expert.domain.manager.repository.ManagerRepository;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.example.expert.exception.CustomException;
import org.example.expert.exception.ExceptionCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagerService {

    private final ManagerRepository managerRepository;
    private final UserRepository userRepository;
    private final TodoRepository todoRepository;

    @Transactional
    public ManagerSaveResponse saveManager(AuthUser authUser, long todoId, ManagerSaveRequest managerSaveRequest) {
        // 일정을 만든 유저
        User user = User.fromAuthUser(authUser);
        Todo todo = todoRepository.findById(todoId)
//                .orElseThrow(() -> new InvalidRequestException("Todo not found"));
                .orElseThrow(() -> new CustomException(ExceptionCode.TODO_NOT_FOUND));

        // ObjectUtils.nullSafeEquals(a, b) - a와 b를 안전하게 비교
//        ObjectUtils.nullSafeEquals(null, null) // true
//        ObjectUtils.nullSafeEquals(1, null)    // false
//        ObjectUtils.nullSafeEquals(1, 1)       // true
//        ObjectUtils.nullSafeEquals(1, 2)       // false
//        if (!ObjectUtils.nullSafeEquals(user.getId(), todo.getUser().getId())) {
//            throw new InvalidRequestException("담당자를 등록하려고 하는 유저가 일정을 만든 유저가 유효하지 않습니다.");
//        }
        // todo.getUser() 가 null인데 null 체크 없이 id를 호출하고 있어서 NullPointerException 문제 발생 <왜 문제가 발생하는것인가?>
        // -> todo.getUser()가 null인데 왜 에러가 발생할까? -> null.getId()를 호출한 것이기 때문에 NullPointerException
        // -> 즉 이 줄에서 아예 코드가 멈춰서 InvalidRequestException 가 발생할 기회도 오지 않음.
        // 그렇기 때문에 null 체크를 먼저 하면 null일 경우 바로 throw로 던져지기 때문에 if문 내부로 들어가게 된다.
        if (todo.getUser() == null || !ObjectUtils.nullSafeEquals(user.getId(), todo.getUser().getId())) {
            throw new InvalidRequestException("담당자를 등록하려고 하는 유저가 일정을 만든 유저가 유효하지 않습니다.");
        }


        User managerUser = userRepository.findById(managerSaveRequest.getManagerUserId())
                .orElseThrow(() -> new InvalidRequestException("등록하려고 하는 담당자 유저가 존재하지 않습니다."));

        if (ObjectUtils.nullSafeEquals(user.getId(), managerUser.getId())) {
            throw new InvalidRequestException("일정 작성자는 본인을 담당자로 등록할 수 없습니다.");
        }

        Manager newManagerUser = new Manager(managerUser, todo);
        Manager savedManagerUser = managerRepository.save(newManagerUser);

        return new ManagerSaveResponse(
                savedManagerUser.getId(),
                new UserResponse(managerUser.getId(), managerUser.getEmail())
        );
    }

    @Transactional(readOnly = true)
    public List<ManagerResponse> getManagers(long todoId) {
        Todo todo = todoRepository.findById(todoId)
//                .orElseThrow(() -> new InvalidRequestException("Todo not found"));
                .orElseThrow(() -> new CustomException(ExceptionCode.TODO_NOT_FOUND));

        List<Manager> managerList = managerRepository.findByTodoIdWithUser(todo.getId());

        List<ManagerResponse> dtoList = new ArrayList<>();
        for (Manager manager : managerList) {
            User user = manager.getUser();
            dtoList.add(new ManagerResponse(
                    manager.getId(),
                    new UserResponse(user.getId(), user.getEmail())
            ));
        }
        return dtoList;
    }

    @Transactional
    public void deleteManager(long userId, long todoId, long managerId) {
        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new InvalidRequestException("User not found"));
                .orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_FOUND));

        Todo todo = todoRepository.findById(todoId)
//                .orElseThrow(() -> new InvalidRequestException("Todo not found"));
                .orElseThrow(() -> new CustomException(ExceptionCode.TODO_NOT_FOUND));

        if (todo.getUser() == null || !ObjectUtils.nullSafeEquals(user.getId(), todo.getUser().getId())) {
            throw new InvalidRequestException("해당 일정을 만든 유저가 유효하지 않습니다.");
        }

        Manager manager = managerRepository.findById(managerId)
//                .orElseThrow(() -> new InvalidRequestException("Manager not found"));
                .orElseThrow(() -> new CustomException(ExceptionCode.MANAGER_NOT_FOUND));

        if (!ObjectUtils.nullSafeEquals(todo.getId(), manager.getTodo().getId())) {
            throw new InvalidRequestException("해당 일정에 등록된 담당자가 아닙니다.");
        }

        managerRepository.delete(manager);
    }
}
