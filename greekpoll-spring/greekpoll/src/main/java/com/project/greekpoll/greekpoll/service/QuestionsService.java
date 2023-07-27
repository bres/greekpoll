package com.project.greekpoll.greekpoll.service;

import com.project.greekpoll.greekpoll.entity.QuestionsEntity;

import java.util.List;

public interface QuestionsService {

    QuestionsEntity findById(int id);

    List<QuestionsEntity> findAll();

    QuestionsEntity save(QuestionsEntity questionsEntity);

    void edit (int id , QuestionsEntity questionsEntity ) ;

    void delete (int id ) ;
}
