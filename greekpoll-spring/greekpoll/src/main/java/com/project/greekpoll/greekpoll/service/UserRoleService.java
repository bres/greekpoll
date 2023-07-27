package com.project.greekpoll.greekpoll.service;



import com.project.greekpoll.greekpoll.entity.Role;

import java.util.List;

public interface UserRoleService {

    Role findById(int id);

    List<Role> findAll();

    Role findByName (String name);

}
