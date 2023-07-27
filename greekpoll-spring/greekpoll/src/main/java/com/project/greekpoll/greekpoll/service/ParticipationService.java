package com.project.greekpoll.greekpoll.service;

import com.project.greekpoll.greekpoll.entity.ParticipationEntity;

import java.util.List;

public interface ParticipationService {


    ParticipationEntity findById(int id);

    List<ParticipationEntity> findAll();

    ParticipationEntity save(ParticipationEntity answersEntity);

    void delete (int id ) ;

}

