package org.ecostanzi.jmint.service;

import org.ecostanzi.jmint.service.dto.TodoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Todo.
 */
public interface TodoService {

    /**
     * Save a todo.
     *
     * @param todoDTO the entity to save
     * @return the persisted entity
     */
    TodoDTO save(TodoDTO todoDTO);

    /**
     *  Get all the todos.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TodoDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" todo.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TodoDTO findOne(Long id);

    /**
     *  Delete the "id" todo.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the todo corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TodoDTO> search(String query, Pageable pageable);
}
