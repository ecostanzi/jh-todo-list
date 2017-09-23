package org.ecostanzi.jmint.service;

import org.ecostanzi.jmint.service.dto.TodoListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing TodoList.
 */
public interface TodoListService {

    /**
     * Save a todoList.
     *
     * @param todoListDTO the entity to save
     * @return the persisted entity
     */
    TodoListDTO save(TodoListDTO todoListDTO);

    /**
     *  Get all the todoLists.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TodoListDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" todoList.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TodoListDTO findOne(Long id);

    /**
     *  Delete the "id" todoList.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the todoList corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TodoListDTO> search(String query, Pageable pageable);
}
