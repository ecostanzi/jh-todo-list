package org.ecostanzi.jmint.service.impl;

import org.ecostanzi.jmint.service.TodoService;
import org.ecostanzi.jmint.domain.Todo;
import org.ecostanzi.jmint.repository.TodoRepository;
import org.ecostanzi.jmint.repository.search.TodoSearchRepository;
import org.ecostanzi.jmint.service.dto.TodoDTO;
import org.ecostanzi.jmint.service.mapper.TodoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Todo.
 */
@Service
@Transactional
public class TodoServiceImpl implements TodoService{

    private final Logger log = LoggerFactory.getLogger(TodoServiceImpl.class);

    private final TodoRepository todoRepository;

    private final TodoMapper todoMapper;

    private final TodoSearchRepository todoSearchRepository;

    public TodoServiceImpl(TodoRepository todoRepository, TodoMapper todoMapper, TodoSearchRepository todoSearchRepository) {
        this.todoRepository = todoRepository;
        this.todoMapper = todoMapper;
        this.todoSearchRepository = todoSearchRepository;
    }

    /**
     * Save a todo.
     *
     * @param todoDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TodoDTO save(TodoDTO todoDTO) {
        log.debug("Request to save Todo : {}", todoDTO);
        Todo todo = todoMapper.toEntity(todoDTO);
        todo = todoRepository.save(todo);
        TodoDTO result = todoMapper.toDto(todo);
        todoSearchRepository.save(todo);
        return result;
    }

    /**
     *  Get all the todos.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TodoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Todos");
        return todoRepository.findAll(pageable)
            .map(todoMapper::toDto);
    }

    /**
     *  Get one todo by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public TodoDTO findOne(Long id) {
        log.debug("Request to get Todo : {}", id);
        Todo todo = todoRepository.findOne(id);
        return todoMapper.toDto(todo);
    }

    /**
     *  Delete the  todo by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Todo : {}", id);
        todoRepository.delete(id);
        todoSearchRepository.delete(id);
    }

    /**
     * Search for the todo corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TodoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Todos for query {}", query);
        Page<Todo> result = todoSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(todoMapper::toDto);
    }
}
