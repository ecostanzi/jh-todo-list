package org.ecostanzi.jmint.repository;

import org.ecostanzi.jmint.domain.Todo;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Todo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TodoRepository extends JpaRepository<Todo, Long>, JpaSpecificationExecutor<Todo> {

    @Query("select todo from Todo todo where todo.author.login = ?#{principal.username}")
    List<Todo> findByAuthorIsCurrentUser();

}
