package com.cloud.secure.streaming.repositories;

import com.cloud.secure.streaming.entities.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 689Cloud
 */
@Repository
@Transactional
public interface SessionRepository extends JpaRepository<Session, String> {

    List<Session> findAllByUserId(String userId);

    void deleteByUserId(String id);
}
