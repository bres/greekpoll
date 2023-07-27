package com.project.greekpoll.greekpoll.entity;

import javax.persistence.*;
import java.util.Objects;

// πανακας συμμετοχης χρηστη σε ερωτησεις ερωτηματολογιου
@Entity
@Table(name = "participation", schema = "greekpoll")
public class ParticipationEntity {
    private Integer id;
    private User userId;
    private AnswersEntity answerId;
    private QuestionsEntity questionId;

    public ParticipationEntity(Integer id, User userId, AnswersEntity answerId, QuestionsEntity questionId) {
        this.id = id;
        this.userId = userId;
        this.answerId = answerId;
        this.questionId = questionId;
    }

    public ParticipationEntity() {

    }


    @Id
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id", referencedColumnName = "id", nullable = false)
    public AnswersEntity getAnswerId() {
        return answerId;
    }

    public void setAnswerId(AnswersEntity answerId) {
        this.answerId = answerId;
    }

    @OneToOne
    @JoinColumn(name = "question_id", referencedColumnName = "id", nullable = false)
    public QuestionsEntity getQuestionId() {
        return questionId;
    }

    public void setQuestionId(QuestionsEntity questionId) {
        this.questionId = questionId;
    }


}
