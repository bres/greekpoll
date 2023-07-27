package com.project.greekpoll.greekpoll.service;


import com.project.greekpoll.greekpoll.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    User save(User user);


    User findById(int id);

    List<User> findAll();

    User findByUsername(String username);


    void edit(int id, User user, String newPassword);

    void changepass(int id, User user);

    void disable(User user);

    void enable( User user);


}
