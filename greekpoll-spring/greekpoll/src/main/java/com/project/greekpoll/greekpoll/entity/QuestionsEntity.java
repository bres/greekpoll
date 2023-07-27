package com.project.greekpoll.greekpoll.entity;

import javax.persistence.*;
import java.util.Objects;

// πανακας ερωτησεων
@Entity
@Table(name = "questions", schema = "greekpoll")
public class QuestionsEntity {
    private Integer id;
    private String text;
    private PollEntity pollId;
    private Integer category;

    @Id
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "text")
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poll_id", referencedColumnName = "id", nullable = false)
    public PollEntity getPollId() {
        return pollId;
    }

    public void setPollId(PollEntity pollId) {
        this.pollId = pollId;
    }

    @Basic
    @Column(name = "category")
    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }


}
