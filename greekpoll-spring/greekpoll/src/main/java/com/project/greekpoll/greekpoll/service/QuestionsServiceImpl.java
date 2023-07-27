package com.project.greekpoll.greekpoll.service;

import com.project.greekpoll.greekpoll.entity.QuestionsEntity;
import com.project.greekpoll.greekpoll.repository.QuestionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionsServiceImpl  implements QuestionsService {

    @Autowired
    private QuestionsRepository questionsRepository;

    public QuestionsServiceImpl(QuestionsRepository questionsRepository) {
        this.questionsRepository = questionsRepository;
    }

    @Override
    public QuestionsEntity findById(int id) {
        return questionsRepository.findById(id);
    }

    @Override
    public List<QuestionsEntity> findAll() {
        return questionsRepository.findAll();
    }

    @Override
    public QuestionsEntity save(QuestionsEntity questionsEntity) {
        return questionsRepository.save(questionsEntity);
    }

    @Override
    public void edit(int id, QuestionsEntity questionsEntity) {
        QuestionsEntity found = questionsRepository.findById(id);

        found.setText(questionsEntity.getText());

        found.setCategory(questionsEntity.getCategory());
        questionsRepository.save(found);
    }

    @Override
    public void delete(int id) {
        questionsRepository.delete(questionsRepository.getById(id));
    }

}
