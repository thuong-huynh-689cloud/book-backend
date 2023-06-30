package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.entities.UserCard;

import java.util.List;

/**
 * @author 689Cloud
 */
public interface CardService {

    UserCard save(UserCard card);

    void delete(UserCard card);

    UserCard getById(String id);

    List<UserCard> getAllByIdIn(List<String> ids);

    void deleteByIdIn(List<String> ids);
}
