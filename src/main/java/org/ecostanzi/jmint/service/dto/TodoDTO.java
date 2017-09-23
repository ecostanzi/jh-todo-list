package org.ecostanzi.jmint.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Todo entity.
 */
public class TodoDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1, max = 40)
    private String text;

    private Long authorId;

    private String authorLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

        TodoDTO todoDTO = (TodoDTO) o;
        if(todoDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), todoDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TodoDTO{" +
            "id=" + getId() +
            ", text='" + getText() + "'" +
            "}";
    }
}
