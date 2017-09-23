package org.ecostanzi.jmint.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.ecostanzi.jmint.service.UserListService;
import org.ecostanzi.jmint.web.rest.util.HeaderUtil;
import org.ecostanzi.jmint.service.dto.UserListDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing UserList.
 */
@RestController
@RequestMapping("/api")
public class UserListResource {

    private final Logger log = LoggerFactory.getLogger(UserListResource.class);

    private static final String ENTITY_NAME = "userList";

    private final UserListService userListService;

    public UserListResource(UserListService userListService) {
        this.userListService = userListService;
    }

    /**
     * POST  /user-lists : Create a new userList.
     *
     * @param userListDTO the userListDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userListDTO, or with status 400 (Bad Request) if the userList has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-lists")
    @Timed
    public ResponseEntity<UserListDTO> createUserList(@Valid @RequestBody UserListDTO userListDTO) throws URISyntaxException {
        log.debug("REST request to save UserList : {}", userListDTO);
        if (userListDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new userList cannot already have an ID")).body(null);
        }
        UserListDTO result = userListService.save(userListDTO);
        return ResponseEntity.created(new URI("/api/user-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-lists : Updates an existing userList.
     *
     * @param userListDTO the userListDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userListDTO,
     * or with status 400 (Bad Request) if the userListDTO is not valid,
     * or with status 500 (Internal Server Error) if the userListDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-lists")
    @Timed
    public ResponseEntity<UserListDTO> updateUserList(@Valid @RequestBody UserListDTO userListDTO) throws URISyntaxException {
        log.debug("REST request to update UserList : {}", userListDTO);
        if (userListDTO.getId() == null) {
            return createUserList(userListDTO);
        }
        UserListDTO result = userListService.save(userListDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, userListDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-lists : get all the userLists.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of userLists in body
     */
    @GetMapping("/user-lists")
    @Timed
    public List<UserListDTO> getAllUserLists() {
        log.debug("REST request to get all UserLists");
        return userListService.findAll();
        }

    /**
     * GET  /user-lists/:id : get the "id" userList.
     *
     * @param id the id of the userListDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userListDTO, or with status 404 (Not Found)
     */
    @GetMapping("/user-lists/{id}")
    @Timed
    public ResponseEntity<UserListDTO> getUserList(@PathVariable Long id) {
        log.debug("REST request to get UserList : {}", id);
        UserListDTO userListDTO = userListService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(userListDTO));
    }

    /**
     * DELETE  /user-lists/:id : delete the "id" userList.
     *
     * @param id the id of the userListDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-lists/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserList(@PathVariable Long id) {
        log.debug("REST request to delete UserList : {}", id);
        userListService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/user-lists?query=:query : search for the userList corresponding
     * to the query.
     *
     * @param query the query of the userList search
     * @return the result of the search
     */
    @GetMapping("/_search/user-lists")
    @Timed
    public List<UserListDTO> searchUserLists(@RequestParam String query) {
        log.debug("REST request to search UserLists for query {}", query);
        return userListService.search(query);
    }

}
