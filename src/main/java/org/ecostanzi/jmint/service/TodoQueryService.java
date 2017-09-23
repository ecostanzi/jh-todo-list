package org.ecostanzi.jmint.service;


import java.time.ZonedDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import org.ecostanzi.jmint.domain.Todo;
import org.ecostanzi.jmint.domain.*; // for static metamodels
import org.ecostanzi.jmint.repository.TodoRepository;
import org.ecostanzi.jmint.repository.search.TodoSearchRepository;
import org.ecostanzi.jmint.service.dto.TodoCriteria;

import org.ecostanzi.jmint.service.dto.TodoDTO;
import org.ecostanzi.jmint.service.mapper.TodoMapper;

/**
 * Service for executing complex queries for Todo entities in the database.
 * The main input is a {@link TodoCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {%link TodoDTO} or a {@link Page} of {%link TodoDTO} which fulfills the criterias
 */
@Service
@Transactional(readOnly = true)
public class TodoQueryService extends QueryService<Todo> {

    private final Logger log = LoggerFactory.getLogger(TodoQueryService.class);


    private final TodoRepository todoRepository;

    private final TodoMapper todoMapper;

    private final TodoSearchRepository todoSearchRepository;

    public TodoQueryService(TodoRepository todoRepository, TodoMapper todoMapper, TodoSearchRepository todoSearchRepository) {
        this.todoRepository = todoRepository;
        this.todoMapper = todoMapper;
        this.todoSearchRepository = todoSearchRepository;
    }

    /**
     * Return a {@link List} of {%link TodoDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TodoDTO> findByCriteria(TodoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Todo> specification = createSpecification(criteria);
        return todoMapper.toDto(todoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {%link TodoDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TodoDTO> findByCriteria(TodoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Todo> specification = createSpecification(criteria);
        final Page<Todo> result = todoRepository.findAll(specification, page);
        return result.map(todoMapper::toDto);
    }

    /**
     * Function to convert TodoCriteria to a {@link Specifications}
     */
    private Specifications<Todo> createSpecification(TodoCriteria criteria) {
        Specifications<Todo> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Todo_.id));
            }
            if (criteria.getText() != null) {
                specification = specification.and(buildStringSpecification(criteria.getText(), Todo_.text));
            }
            if (criteria.getDone() != null) {
                specification = specification.and(buildSpecification(criteria.getDone(), Todo_.done));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Todo_.createdDate));
            }
            if (criteria.getAuthorId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getAuthorId(), Todo_.author, User_.id));
            }
        }
        return specification;
    }

}
