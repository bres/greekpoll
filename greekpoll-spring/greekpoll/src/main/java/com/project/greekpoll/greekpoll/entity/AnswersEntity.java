package com.project.greekpoll.greekpoll.entity;

import javax.persistence.*;
import java.util.Objects;


// πινακας πιθανων απαντησεων ερωτησης
@Entity
@Table(name = "answers", schema = "greekpoll")
public class AnswersEntity {
    private Integer id;
    private QuestionsEntity questionId;
    private String answer;

    @Id
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", referencedColumnName = "id", nullable = false)
    public QuestionsEntity getQuestionId() {
        return questionId;
    }

    public void setQuestionId(QuestionsEntity questionId) {
        this.questionId = questionId;
    }

    @Basic
    @Column(name = "answer")
    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }


}
