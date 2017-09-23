package org.ecostanzi.jmint.service.mapper;

import org.ecostanzi.jmint.domain.*;
import org.ecostanzi.jmint.service.dto.TodoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Todo and its DTO TodoDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface TodoMapper extends EntityMapper <TodoDTO, Todo> {

    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "author.login", target = "authorLogin")
    TodoDTO toDto(Todo todo); 

    @Mapping(source = "authorId", target = "author")
    Todo toEntity(TodoDTO todoDTO); 
    default Todo fromId(Long id) {
        if (id == null) {
            return null;
        }
        Todo todo = new Todo();
        todo.setId(id);
        return todo;
    }
}
