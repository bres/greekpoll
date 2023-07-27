package com.project.greekpoll.greekpoll.repository;

import com.project.greekpoll.greekpoll.entity.QuestionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface QuestionsRepository extends JpaRepository<QuestionsEntity, Integer> {
    QuestionsEntity findById(int id);

    List<QuestionsEntity> findAll();
}
