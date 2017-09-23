package org.ecostanzi.jmint.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.ecostanzi.jmint.service.TodoService;
import org.ecostanzi.jmint.web.rest.util.HeaderUtil;
import org.ecostanzi.jmint.web.rest.util.PaginationUtil;
import org.ecostanzi.jmint.service.dto.TodoDTO;
import org.ecostanzi.jmint.service.dto.TodoCriteria;
import org.ecostanzi.jmint.service.TodoQueryService;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Todo.
 */
@RestController
@RequestMapping("/api")
public class TodoResource {

    private final Logger log = LoggerFactory.getLogger(TodoResource.class);

    private static final String ENTITY_NAME = "todo";

    private final TodoService todoService;

    private final TodoQueryService todoQueryService;

    public TodoResource(TodoService todoService, TodoQueryService todoQueryService) {
        this.todoService = todoService;
        this.todoQueryService = todoQueryService;
    }

    /**
     * POST  /todos : Create a new todo.
     *
     * @param todoDTO the todoDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new todoDTO, or with status 400 (Bad Request) if the todo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/todos")
    @Timed
    public ResponseEntity<TodoDTO> createTodo(@Valid @RequestBody TodoDTO todoDTO) throws URISyntaxException {
        log.debug("REST request to save Todo : {}", todoDTO);
        if (todoDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new todo cannot already have an ID")).body(null);
        }
        TodoDTO result = todoService.save(todoDTO);
        return ResponseEntity.created(new URI("/api/todos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /todos : Updates an existing todo.
     *
     * @param todoDTO the todoDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated todoDTO,
     * or with status 400 (Bad Request) if the todoDTO is not valid,
     * or with status 500 (Internal Server Error) if the todoDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/todos")
    @Timed
    public ResponseEntity<TodoDTO> updateTodo(@Valid @RequestBody TodoDTO todoDTO) throws URISyntaxException {
        log.debug("REST request to update Todo : {}", todoDTO);
        if (todoDTO.getId() == null) {
            return createTodo(todoDTO);
        }
        TodoDTO result = todoService.save(todoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, todoDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /todos : get all the todos.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of todos in body
     */
    @GetMapping("/todos")
    @Timed
    public ResponseEntity<List<TodoDTO>> getAllTodos(TodoCriteria criteria,@ApiParam Pageable pageable) {
        log.debug("REST request to get Todos by criteria: {}", criteria);
        Page<TodoDTO> page = todoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/todos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /todos/:id : get the "id" todo.
     *
     * @param id the id of the todoDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the todoDTO, or with status 404 (Not Found)
     */
    @GetMapping("/todos/{id}")
    @Timed
    public ResponseEntity<TodoDTO> getTodo(@PathVariable Long id) {
        log.debug("REST request to get Todo : {}", id);
        TodoDTO todoDTO = todoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(todoDTO));
    }

    /**
     * DELETE  /todos/:id : delete the "id" todo.
     *
     * @param id the id of the todoDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/todos/{id}")
    @Timed
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        log.debug("REST request to delete Todo : {}", id);
        todoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/todos?query=:query : search for the todo corresponding
     * to the query.
     *
     * @param query the query of the todo search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/todos")
    @Timed
    public ResponseEntity<List<TodoDTO>> searchTodos(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Todos for query {}", query);
        Page<TodoDTO> page = todoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/todos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
