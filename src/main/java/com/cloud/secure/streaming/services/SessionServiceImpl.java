package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.entities.Session;
import com.cloud.secure.streaming.repositories.SessionRepository;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author 689Cloud
 */
@Service
public class SessionServiceImpl implements SessionService {

    final SessionRepository sessionRepository;

    public SessionServiceImpl(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public Session save(Session session) {
        return sessionRepository.save(session);
    }

    @Override
    public void delete(Session session) {
        sessionRepository.delete(session);
    }

    @Override
    public Session getById(String id) {
        return sessionRepository.findById(id).orElse(null);
    }

    @Override
    public List<Session> getAllByUserId(String userId) {
        return sessionRepository.findAllByUserId(userId);
    }

    @Override
    public void deleteAll(List<Session> sessions) {
        sessionRepository.deleteAll(sessions);
    }

    @Override
    public void deleteByUserId(String id) {
        sessionRepository.deleteByUserId(id);
    }
}