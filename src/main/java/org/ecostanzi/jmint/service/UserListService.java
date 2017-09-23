package org.ecostanzi.jmint.service;

import org.ecostanzi.jmint.service.dto.UserListDTO;
import java.util.List;

/**
 * Service Interface for managing UserList.
 */
public interface UserListService {

    /**
     * Save a userList.
     *
     * @param userListDTO the entity to save
     * @return the persisted entity
     */
    UserListDTO save(UserListDTO userListDTO);

    /**
     *  Get all the userLists.
     *
     *  @return the list of entities
     */
    List<UserListDTO> findAll();

    /**
     *  Get the "id" userList.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    UserListDTO findOne(Long id);

    /**
     *  Delete the "id" userList.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the userList corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @return the list of entities
     */
    List<UserListDTO> search(String query);
}
