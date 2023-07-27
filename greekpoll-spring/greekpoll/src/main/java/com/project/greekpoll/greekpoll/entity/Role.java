package com.project.greekpoll.greekpoll.entity;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

// κλαση που χειριζεται αντικειμενα τυπου role -> Ρόλος που εχουν οι χρηστες και διευκολυνει στην εξουσιοδοτηση
@Entity
@Table(name = "role", schema = "greekpoll")
public class Role {
    private Integer id;
    private String name;
    private Collection<User> usersById;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @OneToMany(mappedBy = "roleByUserrole")
    public Collection<User> getUsersById() {
        return usersById;
    }

    public void setUsersById(Collection<User> usersById) {
        this.usersById = usersById;
    }
}
