package com.project.greekpoll.greekpoll.repository;

import com.project.greekpoll.greekpoll.entity.AnswersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AnswersRepository extends JpaRepository<AnswersEntity, Integer> {
    AnswersEntity findById(int id);

    List<AnswersEntity> findAll();
}