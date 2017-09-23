package org.ecostanzi.jmint.service.mapper;

import org.ecostanzi.jmint.domain.*;
import org.ecostanzi.jmint.service.dto.TodoListDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TodoList and its DTO TodoListDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface TodoListMapper extends EntityMapper <TodoListDTO, TodoList> {

    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "author.login", target = "authorLogin")
    TodoListDTO toDto(TodoList todoList); 

    @Mapping(source = "authorId", target = "author")
    TodoList toEntity(TodoListDTO todoListDTO); 
    default TodoList fromId(Long id) {
        if (id == null) {
            return null;
        }
        TodoList todoList = new TodoList();
        todoList.setId(id);
        return todoList;
    }
}
