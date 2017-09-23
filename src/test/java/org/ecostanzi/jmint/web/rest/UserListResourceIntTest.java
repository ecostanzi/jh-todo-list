package org.ecostanzi.jmint.web.rest;

import org.ecostanzi.jmint.JmintApp;

import org.ecostanzi.jmint.domain.UserList;
import org.ecostanzi.jmint.domain.User;
import org.ecostanzi.jmint.domain.TodoList;
import org.ecostanzi.jmint.repository.UserListRepository;
import org.ecostanzi.jmint.service.UserListService;
import org.ecostanzi.jmint.repository.search.UserListSearchRepository;
import org.ecostanzi.jmint.service.dto.UserListDTO;
import org.ecostanzi.jmint.service.mapper.UserListMapper;
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
 * Test class for the UserListResource REST controller.
 *
 * @see UserListResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JmintApp.class)
public class UserListResourceIntTest {

    private static final Boolean DEFAULT_MAIN = false;
    private static final Boolean UPDATED_MAIN = true;

    @Autowired
    private UserListRepository userListRepository;

    @Autowired
    private UserListMapper userListMapper;

    @Autowired
    private UserListService userListService;

    @Autowired
    private UserListSearchRepository userListSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUserListMockMvc;

    private UserList userList;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserListResource userListResource = new UserListResource(userListService);
        this.restUserListMockMvc = MockMvcBuilders.standaloneSetup(userListResource)
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
    public static UserList createEntity(EntityManager em) {
        UserList userList = new UserList()
            .main(DEFAULT_MAIN);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        userList.setUser(user);
        // Add required entity
        TodoList todoList = TodoListResourceIntTest.createEntity(em);
        em.persist(todoList);
        em.flush();
        userList.setTodoList(todoList);
        return userList;
    }

    @Before
    public void initTest() {
        userListSearchRepository.deleteAll();
        userList = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserList() throws Exception {
        int databaseSizeBeforeCreate = userListRepository.findAll().size();

        // Create the UserList
        UserListDTO userListDTO = userListMapper.toDto(userList);
        restUserListMockMvc.perform(post("/api/user-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userListDTO)))
            .andExpect(status().isCreated());

        // Validate the UserList in the database
        List<UserList> userListList = userListRepository.findAll();
        assertThat(userListList).hasSize(databaseSizeBeforeCreate + 1);
        UserList testUserList = userListList.get(userListList.size() - 1);
        assertThat(testUserList.isMain()).isEqualTo(DEFAULT_MAIN);

        // Validate the UserList in Elasticsearch
        UserList userListEs = userListSearchRepository.findOne(testUserList.getId());
        assertThat(userListEs).isEqualToComparingFieldByField(testUserList);
    }

    @Test
    @Transactional
    public void createUserListWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userListRepository.findAll().size();

        // Create the UserList with an existing ID
        userList.setId(1L);
        UserListDTO userListDTO = userListMapper.toDto(userList);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserListMockMvc.perform(post("/api/user-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userListDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserList in the database
        List<UserList> userListList = userListRepository.findAll();
        assertThat(userListList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllUserLists() throws Exception {
        // Initialize the database
        userListRepository.saveAndFlush(userList);

        // Get all the userListList
        restUserListMockMvc.perform(get("/api/user-lists?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userList.getId().intValue())))
            .andExpect(jsonPath("$.[*].main").value(hasItem(DEFAULT_MAIN.booleanValue())));
    }

    @Test
    @Transactional
    public void getUserList() throws Exception {
        // Initialize the database
        userListRepository.saveAndFlush(userList);

        // Get the userList
        restUserListMockMvc.perform(get("/api/user-lists/{id}", userList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userList.getId().intValue()))
            .andExpect(jsonPath("$.main").value(DEFAULT_MAIN.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingUserList() throws Exception {
        // Get the userList
        restUserListMockMvc.perform(get("/api/user-lists/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserList() throws Exception {
        // Initialize the database
        userListRepository.saveAndFlush(userList);
        userListSearchRepository.save(userList);
        int databaseSizeBeforeUpdate = userListRepository.findAll().size();

        // Update the userList
        UserList updatedUserList = userListRepository.findOne(userList.getId());
        updatedUserList
            .main(UPDATED_MAIN);
        UserListDTO userListDTO = userListMapper.toDto(updatedUserList);

        restUserListMockMvc.perform(put("/api/user-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userListDTO)))
            .andExpect(status().isOk());

        // Validate the UserList in the database
        List<UserList> userListList = userListRepository.findAll();
        assertThat(userListList).hasSize(databaseSizeBeforeUpdate);
        UserList testUserList = userListList.get(userListList.size() - 1);
        assertThat(testUserList.isMain()).isEqualTo(UPDATED_MAIN);

        // Validate the UserList in Elasticsearch
        UserList userListEs = userListSearchRepository.findOne(testUserList.getId());
        assertThat(userListEs).isEqualToComparingFieldByField(testUserList);
    }

    @Test
    @Transactional
    public void updateNonExistingUserList() throws Exception {
        int databaseSizeBeforeUpdate = userListRepository.findAll().size();

        // Create the UserList
        UserListDTO userListDTO = userListMapper.toDto(userList);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restUserListMockMvc.perform(put("/api/user-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userListDTO)))
            .andExpect(status().isCreated());

        // Validate the UserList in the database
        List<UserList> userListList = userListRepository.findAll();
        assertThat(userListList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteUserList() throws Exception {
        // Initialize the database
        userListRepository.saveAndFlush(userList);
        userListSearchRepository.save(userList);
        int databaseSizeBeforeDelete = userListRepository.findAll().size();

        // Get the userList
        restUserListMockMvc.perform(delete("/api/user-lists/{id}", userList.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean userListExistsInEs = userListSearchRepository.exists(userList.getId());
        assertThat(userListExistsInEs).isFalse();

        // Validate the database is empty
        List<UserList> userListList = userListRepository.findAll();
        assertThat(userListList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchUserList() throws Exception {
        // Initialize the database
        userListRepository.saveAndFlush(userList);
        userListSearchRepository.save(userList);

        // Search the userList
        restUserListMockMvc.perform(get("/api/_search/user-lists?query=id:" + userList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userList.getId().intValue())))
            .andExpect(jsonPath("$.[*].main").value(hasItem(DEFAULT_MAIN.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserList.class);
        UserList userList1 = new UserList();
        userList1.setId(1L);
        UserList userList2 = new UserList();
        userList2.setId(userList1.getId());
        assertThat(userList1).isEqualTo(userList2);
        userList2.setId(2L);
        assertThat(userList1).isNotEqualTo(userList2);
        userList1.setId(null);
        assertThat(userList1).isNotEqualTo(userList2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserListDTO.class);
        UserListDTO userListDTO1 = new UserListDTO();
        userListDTO1.setId(1L);
        UserListDTO userListDTO2 = new UserListDTO();
        assertThat(userListDTO1).isNotEqualTo(userListDTO2);
        userListDTO2.setId(userListDTO1.getId());
        assertThat(userListDTO1).isEqualTo(userListDTO2);
        userListDTO2.setId(2L);
        assertThat(userListDTO1).isNotEqualTo(userListDTO2);
        userListDTO1.setId(null);
        assertThat(userListDTO1).isNotEqualTo(userListDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(userListMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(userListMapper.fromId(null)).isNull();
    }
}
