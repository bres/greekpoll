package com.project.greekpoll.greekpoll.repository;

import com.project.greekpoll.greekpoll.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findById(int id);

    Role findByName (String name);
    
    List<Role> findAll();
}
