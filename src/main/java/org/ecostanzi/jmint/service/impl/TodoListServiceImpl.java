package org.ecostanzi.jmint.service.impl;

import org.ecostanzi.jmint.service.TodoListService;
import org.ecostanzi.jmint.domain.TodoList;
import org.ecostanzi.jmint.repository.TodoListRepository;
import org.ecostanzi.jmint.repository.search.TodoListSearchRepository;
import org.ecostanzi.jmint.service.dto.TodoListDTO;
import org.ecostanzi.jmint.service.mapper.TodoListMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing TodoList.
 */
@Service
@Transactional
public class TodoListServiceImpl implements TodoListService{

    private final Logger log = LoggerFactory.getLogger(TodoListServiceImpl.class);

    private final TodoListRepository todoListRepository;

    private final TodoListMapper todoListMapper;

    private final TodoListSearchRepository todoListSearchRepository;

    public TodoListServiceImpl(TodoListRepository todoListRepository, TodoListMapper todoListMapper, TodoListSearchRepository todoListSearchRepository) {
        this.todoListRepository = todoListRepository;
        this.todoListMapper = todoListMapper;
        this.todoListSearchRepository = todoListSearchRepository;
    }

    /**
     * Save a todoList.
     *
     * @param todoListDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TodoListDTO save(TodoListDTO todoListDTO) {
        log.debug("Request to save TodoList : {}", todoListDTO);
        TodoList todoList = todoListMapper.toEntity(todoListDTO);
        todoList = todoListRepository.save(todoList);
        TodoListDTO result = todoListMapper.toDto(todoList);
        todoListSearchRepository.save(todoList);
        return result;
    }

    /**
     *  Get all the todoLists.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TodoListDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TodoLists");
        return todoListRepository.findAll(pageable)
            .map(todoListMapper::toDto);
    }

    /**
     *  Get one todoList by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public TodoListDTO findOne(Long id) {
        log.debug("Request to get TodoList : {}", id);
        TodoList todoList = todoListRepository.findOne(id);
        return todoListMapper.toDto(todoList);
    }

    /**
     *  Delete the  todoList by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TodoList : {}", id);
        todoListRepository.delete(id);
        todoListSearchRepository.delete(id);
    }

    /**
     * Search for the todoList corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TodoListDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TodoLists for query {}", query);
        Page<TodoList> result = todoListSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(todoListMapper::toDto);
    }
}
