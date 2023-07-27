package com.project.greekpoll.greekpoll.service;

import com.project.greekpoll.greekpoll.entity.AnswersEntity;
import com.project.greekpoll.greekpoll.repository.AnswersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswersServiceImpl implements AnswersService {


    @Autowired
    private AnswersRepository answersRepository;

    public AnswersServiceImpl(AnswersRepository answersRepository) {
        this.answersRepository = answersRepository;
    }

    @Override
    public AnswersEntity findById(int id) {
        return answersRepository.findById(id);
    }

    @Override
    public List<AnswersEntity> findAll() {
        return answersRepository.findAll();
    }

    @Override
    public AnswersEntity save(AnswersEntity answersEntity) {
        return answersRepository.save(answersEntity);
    }

    @Override
    public void edit(int id, String text) {
        AnswersEntity found = answersRepository.findById(id);
        found.setAnswer(text);
        answersRepository.save(found);
    }

    @Override
    public void delete(int id) {
        AnswersEntity found = answersRepository.findById(id);
        answersRepository.delete(found);
    }
}
