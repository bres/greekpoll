package com.project.greekpoll.greekpoll.service;

import com.project.greekpoll.greekpoll.entity.ParticipationEntity;
import com.project.greekpoll.greekpoll.repository.ParticipationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParticipationServiceImpl implements ParticipationService {

    @Autowired
    private ParticipationRepository participationRepository;


    public ParticipationServiceImpl(ParticipationRepository participationRepository) {
        this.participationRepository = participationRepository;
    }


    @Override
    public ParticipationEntity findById(int id) {
        return participationRepository.findById(id);
    }

    @Override
    public List<ParticipationEntity> findAll() {
        return participationRepository.findAll();
    }

    @Override
    public ParticipationEntity save(ParticipationEntity participationEntity) {
        return participationRepository.save(participationEntity);
    }


    @Override
    public void delete(int id) {
        participationRepository.delete(participationRepository.findById(id));
    }
}
