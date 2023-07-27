package com.project.greekpoll.greekpoll.service;


import com.project.greekpoll.greekpoll.entity.AnswersEntity;

import java.util.List;

public interface AnswersService {

    AnswersEntity findById(int id);

    List<AnswersEntity> findAll();

    AnswersEntity save(AnswersEntity answersEntity);

    void edit (int id , String text ) ;

    void delete (int id ) ;

}