package org.ecostanzi.jmint.repository;

import org.ecostanzi.jmint.domain.TodoList;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the TodoList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TodoListRepository extends JpaRepository<TodoList, Long> {

    @Query("select todo_list from TodoList todo_list where todo_list.author.login = ?#{principal.username}")
    List<TodoList> findByAuthorIsCurrentUser();

}
