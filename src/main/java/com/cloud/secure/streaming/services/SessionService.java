package com.cloud.secure.streaming.services;


import com.cloud.secure.streaming.entities.Session;

import java.util.List;

/**
 * @author 689Cloud
 */
public interface SessionService {

    Session save(Session session);

    void delete(Session session);

    Session getById(String id);

    List<Session> getAllByUserId(String userId);

    void deleteAll(List<Session> sessions);

    void deleteByUserId(String id);

}
