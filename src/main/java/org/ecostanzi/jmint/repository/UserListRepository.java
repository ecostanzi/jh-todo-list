package org.ecostanzi.jmint.repository;

import org.ecostanzi.jmint.domain.UserList;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the UserList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserListRepository extends JpaRepository<UserList, Long> {

    @Query("select user_list from UserList user_list where user_list.user.login = ?#{principal.username}")
    List<UserList> findByUserIsCurrentUser();

}
