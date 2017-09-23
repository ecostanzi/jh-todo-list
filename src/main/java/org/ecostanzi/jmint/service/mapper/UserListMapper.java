package org.ecostanzi.jmint.service.mapper;

import org.ecostanzi.jmint.domain.*;
import org.ecostanzi.jmint.service.dto.UserListDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity UserList and its DTO UserListDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, TodoListMapper.class, })
public interface UserListMapper extends EntityMapper <UserListDTO, UserList> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")

    @Mapping(source = "todoList.id", target = "todoListId")
    UserListDTO toDto(UserList userList); 

    @Mapping(source = "userId", target = "user")

    @Mapping(source = "todoListId", target = "todoList")
    UserList toEntity(UserListDTO userListDTO); 
    default UserList fromId(Long id) {
        if (id == null) {
            return null;
        }
        UserList userList = new UserList();
        userList.setId(id);
        return userList;
    }
}
