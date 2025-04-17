package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {

//     JPA에서 Todo를 기준으로 user와 함께 조회함. -> 그 결과를 modifieadAt 기준으로 내림차수 정렬
//     FETCH를 이용해 user와 관련된 쿼리를 한번에 조회함
//    @Query("SELECT t " +
//            "FROM Todo t LEFT JOIN FETCH t.user u " +
//            "ORDER BY t.modifiedAt DESC")
//    Page<Todo> findAllByOrderByModifiedAtDesc(Pageable pageable);
//
    @Query("SELECT t FROM Todo t " +
            "LEFT JOIN FETCH t.user " +
            "WHERE t.id = :todoId")
    Optional<Todo> findByIdWithUser(@Param("todoId") Long todoId);

//     @EntityGraph란 JPA에서 연관된 엔티티를 미리 가져오도록 지정하는 방법
//    (fetch join을 대신해서 사용하는 방법 중 하나)
//    fetch join은 JPQL에서 쓰는 방식
//    @EntityGraph(attributePaths = {"user"}) 이런식으로 사용
//    @EntityGraph 연관된 엔티티를 즉시 로딩하도록 설정
//    attributePaths - 어떤 연관 엔티티를 fetch할지 지정 ("user", "user.profile" 등)

    @EntityGraph(attributePaths = {"user"})
    Page<Todo> findAllByOrderByModifiedAtDesc(Pageable pageable);

//    getTodos 메서드와 관련되어 있지는 않아서 주석처리했지만 이런식으로도 바꿀 수 있음 (비추천)
//    @EntityGraph(attributePaths = {"user"})
//    @Query("SELECT t " +
//            "FROM Todo t " +
//            "WHERE t.id = :todoId")
//    Optional<Todo> findByIdWithUser(@Param("todoId") Long todoId);

    int countById(Long todoId);
}
