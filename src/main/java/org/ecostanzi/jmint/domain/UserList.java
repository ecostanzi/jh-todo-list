package org.ecostanzi.jmint.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A UserList.
 */
@Entity
@Table(name = "user_list")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "userlist")
public class UserList implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "main")
    private Boolean main;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    @ManyToOne(optional = false)
    @NotNull
    private TodoList todoList;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isMain() {
        return main;
    }

    public UserList main(Boolean main) {
        this.main = main;
        return this;
    }

    public void setMain(Boolean main) {
        this.main = main;
    }

    public User getUser() {
        return user;
    }

    public UserList user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TodoList getTodoList() {
        return todoList;
    }

    public UserList todoList(TodoList todoList) {
        this.todoList = todoList;
        return this;
    }

    public void setTodoList(TodoList todoList) {
        this.todoList = todoList;
    }
    // jhipster-needle-entity-add-getters-setters - Jhipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserList userList = (UserList) o;
        if (userList.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userList.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserList{" +
            "id=" + getId() +
            ", main='" + isMain() + "'" +
            "}";
    }
}
