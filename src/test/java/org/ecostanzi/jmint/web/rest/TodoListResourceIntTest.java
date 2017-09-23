package org.ecostanzi.jmint.web.rest;

import org.ecostanzi.jmint.JmintApp;

import org.ecostanzi.jmint.domain.TodoList;
import org.ecostanzi.jmint.domain.User;
import org.ecostanzi.jmint.repository.TodoListRepository;
import org.ecostanzi.jmint.service.TodoListService;
import org.ecostanzi.jmint.repository.search.TodoListSearchRepository;
import org.ecostanzi.jmint.service.dto.TodoListDTO;
import org.ecostanzi.jmint.service.mapper.TodoListMapper;
import org.ecostanzi.jmint.web.rest.errors.ExceptionTranslator;

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
 * Test class for the TodoListResource REST controller.
 *
 * @see TodoListResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JmintApp.class)
public class TodoListResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private TodoListRepository todoListRepository;

    @Autowired
    private TodoListMapper todoListMapper;

    @Autowired
    private TodoListService todoListService;

    @Autowired
    private TodoListSearchRepository todoListSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTodoListMockMvc;

    private TodoList todoList;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TodoListResource todoListResource = new TodoListResource(todoListService);
        this.restTodoListMockMvc = MockMvcBuilders.standaloneSetup(todoListResource)
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
    public static TodoList createEntity(EntityManager em) {
        TodoList todoList = new TodoList()
            .name(DEFAULT_NAME);
        // Add required entity
        User author = UserResourceIntTest.createEntity(em);
        em.persist(author);
        em.flush();
        todoList.setAuthor(author);
        return todoList;
    }

    @Before
    public void initTest() {
        todoListSearchRepository.deleteAll();
        todoList = createEntity(em);
    }

    @Test
    @Transactional
    public void createTodoList() throws Exception {
        int databaseSizeBeforeCreate = todoListRepository.findAll().size();

        // Create the TodoList
        TodoListDTO todoListDTO = todoListMapper.toDto(todoList);
        restTodoListMockMvc.perform(post("/api/todo-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(todoListDTO)))
            .andExpect(status().isCreated());

        // Validate the TodoList in the database
        List<TodoList> todoListList = todoListRepository.findAll();
        assertThat(todoListList).hasSize(databaseSizeBeforeCreate + 1);
        TodoList testTodoList = todoListList.get(todoListList.size() - 1);
        assertThat(testTodoList.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the TodoList in Elasticsearch
        TodoList todoListEs = todoListSearchRepository.findOne(testTodoList.getId());
        assertThat(todoListEs).isEqualToComparingFieldByField(testTodoList);
    }

    @Test
    @Transactional
    public void createTodoListWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = todoListRepository.findAll().size();

        // Create the TodoList with an existing ID
        todoList.setId(1L);
        TodoListDTO todoListDTO = todoListMapper.toDto(todoList);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTodoListMockMvc.perform(post("/api/todo-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(todoListDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TodoList in the database
        List<TodoList> todoListList = todoListRepository.findAll();
        assertThat(todoListList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = todoListRepository.findAll().size();
        // set the field null
        todoList.setName(null);

        // Create the TodoList, which fails.
        TodoListDTO todoListDTO = todoListMapper.toDto(todoList);

        restTodoListMockMvc.perform(post("/api/todo-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(todoListDTO)))
            .andExpect(status().isBadRequest());

        List<TodoList> todoListList = todoListRepository.findAll();
        assertThat(todoListList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTodoLists() throws Exception {
        // Initialize the database
        todoListRepository.saveAndFlush(todoList);

        // Get all the todoListList
        restTodoListMockMvc.perform(get("/api/todo-lists?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(todoList.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getTodoList() throws Exception {
        // Initialize the database
        todoListRepository.saveAndFlush(todoList);

        // Get the todoList
        restTodoListMockMvc.perform(get("/api/todo-lists/{id}", todoList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(todoList.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTodoList() throws Exception {
        // Get the todoList
        restTodoListMockMvc.perform(get("/api/todo-lists/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTodoList() throws Exception {
        // Initialize the database
        todoListRepository.saveAndFlush(todoList);
        todoListSearchRepository.save(todoList);
        int databaseSizeBeforeUpdate = todoListRepository.findAll().size();

        // Update the todoList
        TodoList updatedTodoList = todoListRepository.findOne(todoList.getId());
        updatedTodoList
            .name(UPDATED_NAME);
        TodoListDTO todoListDTO = todoListMapper.toDto(updatedTodoList);

        restTodoListMockMvc.perform(put("/api/todo-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(todoListDTO)))
            .andExpect(status().isOk());

        // Validate the TodoList in the database
        List<TodoList> todoListList = todoListRepository.findAll();
        assertThat(todoListList).hasSize(databaseSizeBeforeUpdate);
        TodoList testTodoList = todoListList.get(todoListList.size() - 1);
        assertThat(testTodoList.getName()).isEqualTo(UPDATED_NAME);

        // Validate the TodoList in Elasticsearch
        TodoList todoListEs = todoListSearchRepository.findOne(testTodoList.getId());
        assertThat(todoListEs).isEqualToComparingFieldByField(testTodoList);
    }

    @Test
    @Transactional
    public void updateNonExistingTodoList() throws Exception {
        int databaseSizeBeforeUpdate = todoListRepository.findAll().size();

        // Create the TodoList
        TodoListDTO todoListDTO = todoListMapper.toDto(todoList);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTodoListMockMvc.perform(put("/api/todo-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(todoListDTO)))
            .andExpect(status().isCreated());

        // Validate the TodoList in the database
        List<TodoList> todoListList = todoListRepository.findAll();
        assertThat(todoListList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTodoList() throws Exception {
        // Initialize the database
        todoListRepository.saveAndFlush(todoList);
        todoListSearchRepository.save(todoList);
        int databaseSizeBeforeDelete = todoListRepository.findAll().size();

        // Get the todoList
        restTodoListMockMvc.perform(delete("/api/todo-lists/{id}", todoList.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean todoListExistsInEs = todoListSearchRepository.exists(todoList.getId());
        assertThat(todoListExistsInEs).isFalse();

        // Validate the database is empty
        List<TodoList> todoListList = todoListRepository.findAll();
        assertThat(todoListList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTodoList() throws Exception {
        // Initialize the database
        todoListRepository.saveAndFlush(todoList);
        todoListSearchRepository.save(todoList);

        // Search the todoList
        restTodoListMockMvc.perform(get("/api/_search/todo-lists?query=id:" + todoList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(todoList.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TodoList.class);
        TodoList todoList1 = new TodoList();
        todoList1.setId(1L);
        TodoList todoList2 = new TodoList();
        todoList2.setId(todoList1.getId());
        assertThat(todoList1).isEqualTo(todoList2);
        todoList2.setId(2L);
        assertThat(todoList1).isNotEqualTo(todoList2);
        todoList1.setId(null);
        assertThat(todoList1).isNotEqualTo(todoList2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TodoListDTO.class);
        TodoListDTO todoListDTO1 = new TodoListDTO();
        todoListDTO1.setId(1L);
        TodoListDTO todoListDTO2 = new TodoListDTO();
        assertThat(todoListDTO1).isNotEqualTo(todoListDTO2);
        todoListDTO2.setId(todoListDTO1.getId());
        assertThat(todoListDTO1).isEqualTo(todoListDTO2);
        todoListDTO2.setId(2L);
        assertThat(todoListDTO1).isNotEqualTo(todoListDTO2);
        todoListDTO1.setId(null);
        assertThat(todoListDTO1).isNotEqualTo(todoListDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(todoListMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(todoListMapper.fromId(null)).isNull();
    }
}
