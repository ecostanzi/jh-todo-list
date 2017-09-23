package org.ecostanzi.jmint.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the UserList entity.
 */
public class UserListDTO implements Serializable {

    private Long id;

    private Boolean main;

    private Long userId;

    private String userLogin;

    private Long todoListId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isMain() {
        return main;
    }

    public void setMain(Boolean main) {
        this.main = main;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public Long getTodoListId() {
        return todoListId;
    }

    public void setTodoListId(Long todoListId) {
        this.todoListId = todoListId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserListDTO userListDTO = (UserListDTO) o;
        if(userListDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userListDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserListDTO{" +
            "id=" + getId() +
            ", main='" + isMain() + "'" +
            "}";
    }
}
