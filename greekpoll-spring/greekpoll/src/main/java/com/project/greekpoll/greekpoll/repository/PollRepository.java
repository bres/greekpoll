package com.project.greekpoll.greekpoll.repository;

import com.project.greekpoll.greekpoll.entity.PollEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PollRepository extends JpaRepository<PollEntity, Integer> {
    PollEntity findById(int id);

    List<PollEntity> findAll();
}