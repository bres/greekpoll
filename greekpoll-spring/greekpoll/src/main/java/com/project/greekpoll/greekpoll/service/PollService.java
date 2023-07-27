package com.project.greekpoll.greekpoll.service;

import com.project.greekpoll.greekpoll.entity.PollEntity;

import java.util.List;

public interface PollService {

    PollEntity findById(int id);

    List<PollEntity> findAll();

    PollEntity save(PollEntity pollEntity);

    void edit (int id , PollEntity pollEntity ) ;

}