package org.ecostanzi.jmint.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.ecostanzi.jmint.service.TodoListService;
import org.ecostanzi.jmint.web.rest.util.HeaderUtil;
import org.ecostanzi.jmint.web.rest.util.PaginationUtil;
import org.ecostanzi.jmint.service.dto.TodoListDTO;
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
 * REST controller for managing TodoList.
 */
@RestController
@RequestMapping("/api")
public class TodoListResource {

    private final Logger log = LoggerFactory.getLogger(TodoListResource.class);

    private static final String ENTITY_NAME = "todoList";

    private final TodoListService todoListService;

    public TodoListResource(TodoListService todoListService) {
        this.todoListService = todoListService;
    }

    /**
     * POST  /todo-lists : Create a new todoList.
     *
     * @param todoListDTO the todoListDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new todoListDTO, or with status 400 (Bad Request) if the todoList has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/todo-lists")
    @Timed
    public ResponseEntity<TodoListDTO> createTodoList(@Valid @RequestBody TodoListDTO todoListDTO) throws URISyntaxException {
        log.debug("REST request to save TodoList : {}", todoListDTO);
        if (todoListDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new todoList cannot already have an ID")).body(null);
        }
        TodoListDTO result = todoListService.save(todoListDTO);
        return ResponseEntity.created(new URI("/api/todo-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /todo-lists : Updates an existing todoList.
     *
     * @param todoListDTO the todoListDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated todoListDTO,
     * or with status 400 (Bad Request) if the todoListDTO is not valid,
     * or with status 500 (Internal Server Error) if the todoListDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/todo-lists")
    @Timed
    public ResponseEntity<TodoListDTO> updateTodoList(@Valid @RequestBody TodoListDTO todoListDTO) throws URISyntaxException {
        log.debug("REST request to update TodoList : {}", todoListDTO);
        if (todoListDTO.getId() == null) {
            return createTodoList(todoListDTO);
        }
        TodoListDTO result = todoListService.save(todoListDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, todoListDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /todo-lists : get all the todoLists.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of todoLists in body
     */
    @GetMapping("/todo-lists")
    @Timed
    public ResponseEntity<List<TodoListDTO>> getAllTodoLists(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of TodoLists");
        Page<TodoListDTO> page = todoListService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/todo-lists");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /todo-lists/:id : get the "id" todoList.
     *
     * @param id the id of the todoListDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the todoListDTO, or with status 404 (Not Found)
     */
    @GetMapping("/todo-lists/{id}")
    @Timed
    public ResponseEntity<TodoListDTO> getTodoList(@PathVariable Long id) {
        log.debug("REST request to get TodoList : {}", id);
        TodoListDTO todoListDTO = todoListService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(todoListDTO));
    }

    /**
     * DELETE  /todo-lists/:id : delete the "id" todoList.
     *
     * @param id the id of the todoListDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/todo-lists/{id}")
    @Timed
    public ResponseEntity<Void> deleteTodoList(@PathVariable Long id) {
        log.debug("REST request to delete TodoList : {}", id);
        todoListService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/todo-lists?query=:query : search for the todoList corresponding
     * to the query.
     *
     * @param query the query of the todoList search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/todo-lists")
    @Timed
    public ResponseEntity<List<TodoListDTO>> searchTodoLists(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of TodoLists for query {}", query);
        Page<TodoListDTO> page = todoListService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/todo-lists");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
