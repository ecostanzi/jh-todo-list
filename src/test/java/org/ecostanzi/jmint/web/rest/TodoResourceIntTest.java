package org.ecostanzi.jmint.web.rest;

import org.ecostanzi.jmint.JmintApp;

import org.ecostanzi.jmint.domain.Todo;
import org.ecostanzi.jmint.domain.User;
import org.ecostanzi.jmint.repository.TodoRepository;
import org.ecostanzi.jmint.service.TodoService;
import org.ecostanzi.jmint.repository.search.TodoSearchRepository;
import org.ecostanzi.jmint.service.dto.TodoDTO;
import org.ecostanzi.jmint.service.mapper.TodoMapper;
import org.ecostanzi.jmint.web.rest.errors.ExceptionTranslator;
import org.ecostanzi.jmint.service.dto.TodoCriteria;
import org.ecostanzi.jmint.service.TodoQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TodoResource REST controller.
 *
 * @see TodoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JmintApp.class)
public class TodoResourceIntTest {

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private TodoMapper todoMapper;

    @Autowired
    private TodoService todoService;

    @Autowired
    private TodoSearchRepository todoSearchRepository;

    @Autowired
    private TodoQueryService todoQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTodoMockMvc;

    private Todo todo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TodoResource todoResource = new TodoResource(todoService, todoQueryService);
        this.restTodoMockMvc = MockMvcBuilders.standaloneSetup(todoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Todo createEntity(EntityManager em) {
        Todo todo = new Todo()
            .text(DEFAULT_TEXT);
        // Add required entity
        User author = UserResourceIntTest.createEntity(em);
        em.persist(author);
        em.flush();
        todo.setAuthor(author);
        return todo;
    }

    @Before
    public void initTest() {
        todoSearchRepository.deleteAll();
        todo = createEntity(em);
    }

    @Test
    @Transactional
    public void createTodo() throws Exception {
        int databaseSizeBeforeCreate = todoRepository.findAll().size();

        // Create the Todo
        TodoDTO todoDTO = todoMapper.toDto(todo);
        restTodoMockMvc.perform(post("/api/todos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(todoDTO)))
            .andExpect(status().isCreated());

        // Validate the Todo in the database
        List<Todo> todoList = todoRepository.findAll();
        assertThat(todoList).hasSize(databaseSizeBeforeCreate + 1);
        Todo testTodo = todoList.get(todoList.size() - 1);
        assertThat(testTodo.getText()).isEqualTo(DEFAULT_TEXT);

        // Validate the Todo in Elasticsearch
        Todo todoEs = todoSearchRepository.findOne(testTodo.getId());
        assertThat(todoEs).isEqualToComparingFieldByField(testTodo);
    }

    @Test
    @Transactional
    public void createTodoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = todoRepository.findAll().size();

        // Create the Todo with an existing ID
        todo.setId(1L);
        TodoDTO todoDTO = todoMapper.toDto(todo);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTodoMockMvc.perform(post("/api/todos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(todoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Todo in the database
        List<Todo> todoList = todoRepository.findAll();
        assertThat(todoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = todoRepository.findAll().size();
        // set the field null
        todo.setText(null);

        // Create the Todo, which fails.
        TodoDTO todoDTO = todoMapper.toDto(todo);

        restTodoMockMvc.perform(post("/api/todos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(todoDTO)))
            .andExpect(status().isBadRequest());

        List<Todo> todoList = todoRepository.findAll();
        assertThat(todoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTodos() throws Exception {
        // Initialize the database
        todoRepository.saveAndFlush(todo);

        // Get all the todoList
        restTodoMockMvc.perform(get("/api/todos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(todo.getId().intValue())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())));
    }

    @Test
    @Transactional
    public void getTodo() throws Exception {
        // Initialize the database
        todoRepository.saveAndFlush(todo);

        // Get the todo
        restTodoMockMvc.perform(get("/api/todos/{id}", todo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(todo.getId().intValue()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT.toString()));
    }

    @Test
    @Transactional
    public void getAllTodosByTextIsEqualToSomething() throws Exception {
        // Initialize the database
        todoRepository.saveAndFlush(todo);

        // Get all the todoList where text equals to DEFAULT_TEXT
        defaultTodoShouldBeFound("text.equals=" + DEFAULT_TEXT);

        // Get all the todoList where text equals to UPDATED_TEXT
        defaultTodoShouldNotBeFound("text.equals=" + UPDATED_TEXT);
    }

    @Test
    @Transactional
    public void getAllTodosByTextIsInShouldWork() throws Exception {
        // Initialize the database
        todoRepository.saveAndFlush(todo);

        // Get all the todoList where text in DEFAULT_TEXT or UPDATED_TEXT
        defaultTodoShouldBeFound("text.in=" + DEFAULT_TEXT + "," + UPDATED_TEXT);

        // Get all the todoList where text equals to UPDATED_TEXT
        defaultTodoShouldNotBeFound("text.in=" + UPDATED_TEXT);
    }

    @Test
    @Transactional
    public void getAllTodosByTextIsNullOrNotNull() throws Exception {
        // Initialize the database
        todoRepository.saveAndFlush(todo);

        // Get all the todoList where text is not null
        defaultTodoShouldBeFound("text.specified=true");

        // Get all the todoList where text is null
        defaultTodoShouldNotBeFound("text.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultTodoShouldBeFound(String filter) throws Exception {
        restTodoMockMvc.perform(get("/api/todos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(todo.getId().intValue())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultTodoShouldNotBeFound(String filter) throws Exception {
        restTodoMockMvc.perform(get("/api/todos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingTodo() throws Exception {
        // Get the todo
        restTodoMockMvc.perform(get("/api/todos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTodo() throws Exception {
        // Initialize the database
        todoRepository.saveAndFlush(todo);
        todoSearchRepository.save(todo);
        int databaseSizeBeforeUpdate = todoRepository.findAll().size();

        // Update the todo
        Todo updatedTodo = todoRepository.findOne(todo.getId());
        updatedTodo
            .text(UPDATED_TEXT);
        TodoDTO todoDTO = todoMapper.toDto(updatedTodo);

        restTodoMockMvc.perform(put("/api/todos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(todoDTO)))
            .andExpect(status().isOk());

        // Validate the Todo in the database
        List<Todo> todoList = todoRepository.findAll();
        assertThat(todoList).hasSize(databaseSizeBeforeUpdate);
        Todo testTodo = todoList.get(todoList.size() - 1);
        assertThat(testTodo.getText()).isEqualTo(UPDATED_TEXT);

        // Validate the Todo in Elasticsearch
        Todo todoEs = todoSearchRepository.findOne(testTodo.getId());
        assertThat(todoEs).isEqualToComparingFieldByField(testTodo);
    }

    @Test
    @Transactional
    public void updateNonExistingTodo() throws Exception {
        int databaseSizeBeforeUpdate = todoRepository.findAll().size();

        // Create the Todo
        TodoDTO todoDTO = todoMapper.toDto(todo);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTodoMockMvc.perform(put("/api/todos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(todoDTO)))
            .andExpect(status().isCreated());

        // Validate the Todo in the database
        List<Todo> todoList = todoRepository.findAll();
        assertThat(todoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTodo() throws Exception {
        // Initialize the database
        todoRepository.saveAndFlush(todo);
        todoSearchRepository.save(todo);
        int databaseSizeBeforeDelete = todoRepository.findAll().size();

        // Get the todo
        restTodoMockMvc.perform(delete("/api/todos/{id}", todo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean todoExistsInEs = todoSearchRepository.exists(todo.getId());
        assertThat(todoExistsInEs).isFalse();

        // Validate the database is empty
        List<Todo> todoList = todoRepository.findAll();
        assertThat(todoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTodo() throws Exception {
        // Initialize the database
        todoRepository.saveAndFlush(todo);
        todoSearchRepository.save(todo);

        // Search the todo
        restTodoMockMvc.perform(get("/api/_search/todos?query=id:" + todo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(todo.getId().intValue())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Todo.class);
        Todo todo1 = new Todo();
        todo1.setId(1L);
        Todo todo2 = new Todo();
        todo2.setId(todo1.getId());
        assertThat(todo1).isEqualTo(todo2);
        todo2.setId(2L);
        assertThat(todo1).isNotEqualTo(todo2);
        todo1.setId(null);
        assertThat(todo1).isNotEqualTo(todo2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TodoDTO.class);
        TodoDTO todoDTO1 = new TodoDTO();
        todoDTO1.setId(1L);
        TodoDTO todoDTO2 = new TodoDTO();
        assertThat(todoDTO1).isNotEqualTo(todoDTO2);
        todoDTO2.setId(todoDTO1.getId());
        assertThat(todoDTO1).isEqualTo(todoDTO2);
        todoDTO2.setId(2L);
        assertThat(todoDTO1).isNotEqualTo(todoDTO2);
        todoDTO1.setId(null);
        assertThat(todoDTO1).isNotEqualTo(todoDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(todoMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(todoMapper.fromId(null)).isNull();
    }
}
