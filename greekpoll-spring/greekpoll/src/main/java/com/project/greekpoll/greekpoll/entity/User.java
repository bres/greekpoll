package com.project.greekpoll.greekpoll.entity;

import javax.persistence.*;
import java.util.Collection;

// κλαση που χειριζεται αντικειμενα τυπου user -> Χρήστες
@Entity
@Table(name = "user", schema = "greekpoll")
public class User {
    private Integer id;
    private String gender;
    private String fullname;
    private String prefecture;
    private String municipality;
    private String password;
    private String username;
    private Role roleByUserrole;


    public User() {
    }

    public User(String gender, String fullname, String prefecture, String municipality, String password, String username, Role roleByUserrole) {
        this.gender = gender;
        this.fullname = fullname;
        this.prefecture = prefecture;
        this.municipality = municipality;
        this.password = password;
        this.username = username;
        this.roleByUserrole = roleByUserrole;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    @Basic
    @Column(name = "gender")
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Basic
    @Column(name = "fullname")
    public String getFullname() {
        return fullname;
    }


    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    @Basic
    @Column(name = "prefecture")
    public String getPrefecture() {
        return prefecture;
    }

    public void setPrefecture(String prefecture) {
        this.prefecture = prefecture;
    }

    @Basic
    @Column(name = "municipality")
    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @ManyToOne
    @JoinColumn(name = "userrole", referencedColumnName = "id", nullable = false)
    public Role getRoleByUserrole() {
        return roleByUserrole;
    }

    public void setRoleByUserrole(Role roleByUserrole) {
        this.roleByUserrole = roleByUserrole;
    }


}
