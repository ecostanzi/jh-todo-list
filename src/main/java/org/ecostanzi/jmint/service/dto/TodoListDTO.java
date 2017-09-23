package org.ecostanzi.jmint.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the TodoList entity.
 */
public class TodoListDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1, max = 20)
    private String name;

    private Long authorId;

    private String authorLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long userId) {
        this.authorId = userId;
    }

    public String getAuthorLogin() {
        return authorLogin;
    }

    public void setAuthorLogin(String userLogin) {
        this.authorLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TodoListDTO todoListDTO = (TodoListDTO) o;
        if(todoListDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), todoListDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TodoListDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
