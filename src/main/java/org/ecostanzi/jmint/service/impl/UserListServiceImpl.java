package org.ecostanzi.jmint.service.impl;

import org.ecostanzi.jmint.service.UserListService;
import org.ecostanzi.jmint.domain.UserList;
import org.ecostanzi.jmint.repository.UserListRepository;
import org.ecostanzi.jmint.repository.search.UserListSearchRepository;
import org.ecostanzi.jmint.service.dto.UserListDTO;
import org.ecostanzi.jmint.service.mapper.UserListMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing UserList.
 */
@Service
@Transactional
public class UserListServiceImpl implements UserListService{

    private final Logger log = LoggerFactory.getLogger(UserListServiceImpl.class);

    private final UserListRepository userListRepository;

    private final UserListMapper userListMapper;

    private final UserListSearchRepository userListSearchRepository;

    public UserListServiceImpl(UserListRepository userListRepository, UserListMapper userListMapper, UserListSearchRepository userListSearchRepository) {
        this.userListRepository = userListRepository;
        this.userListMapper = userListMapper;
        this.userListSearchRepository = userListSearchRepository;
    }

    /**
     * Save a userList.
     *
     * @param userListDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public UserListDTO save(UserListDTO userListDTO) {
        log.debug("Request to save UserList : {}", userListDTO);
        UserList userList = userListMapper.toEntity(userListDTO);
        userList = userListRepository.save(userList);
        UserListDTO result = userListMapper.toDto(userList);
        userListSearchRepository.save(userList);
        return result;
    }

    /**
     *  Get all the userLists.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserListDTO> findAll() {
        log.debug("Request to get all UserLists");
        return userListRepository.findAll().stream()
            .map(userListMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one userList by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public UserListDTO findOne(Long id) {
        log.debug("Request to get UserList : {}", id);
        UserList userList = userListRepository.findOne(id);
        return userListMapper.toDto(userList);
    }

    /**
     *  Delete the  userList by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserList : {}", id);
        userListRepository.delete(id);
        userListSearchRepository.delete(id);
    }

    /**
     * Search for the userList corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserListDTO> search(String query) {
        log.debug("Request to search UserLists for query {}", query);
        return StreamSupport
            .stream(userListSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(userListMapper::toDto)
            .collect(Collectors.toList());
    }
}
