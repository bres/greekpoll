package com.project.greekpoll.greekpoll.service;

import com.project.greekpoll.greekpoll.entity.PollEntity;
import com.project.greekpoll.greekpoll.repository.PollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PollServiceImpl implements PollService {


    @Autowired
    private PollRepository pollRepository;

    public PollServiceImpl(PollRepository pollRepository) {
        this.pollRepository = pollRepository;
    }

    @Override
    public PollEntity findById(int id) {
        return pollRepository.findById(id);
    }

    @Override
    public List<PollEntity> findAll() {
        return pollRepository.findAll();
    }

    @Override
    public PollEntity save(PollEntity pollEntity) {
        return pollRepository.save(pollEntity);
    }

    @Override
    public void edit(int id, PollEntity pollEntity) {
        PollEntity found = pollRepository.findById(id);
        found.setTitle(pollEntity.getTitle());
        found.setStartdate(pollEntity.getStartdate());
        found.setEnddate(pollEntity.getEnddate());
        pollRepository.save(found);
    }
}
