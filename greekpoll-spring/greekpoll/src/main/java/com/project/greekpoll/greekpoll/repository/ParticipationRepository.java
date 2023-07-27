package com.project.greekpoll.greekpoll.repository;

import com.project.greekpoll.greekpoll.entity.ParticipationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipationRepository extends JpaRepository<ParticipationEntity, Integer> {
    ParticipationEntity findById(int id);

    List<ParticipationEntity> findAll();
}