package com.cloud.secure.streaming.services;


import com.cloud.secure.streaming.common.enums.Status;
import com.cloud.secure.streaming.common.enums.UserRole;
import com.cloud.secure.streaming.entities.User;

import java.util.List;

public interface UserService {

    User getByEmailUserRole(String email, UserRole userRole);

    User getById(String id);

    User getUserBySessionId(String sessionId);

    User getByEmailAndStatus(String email);

    User saveUser(User user);

    void saveAll(List<User> user);

    List<User> getAllByIdInAndStatus(List<String> ids, Status status);

//    Page<User> getByLastNameAndFirstNameContaining(String name, boolean isAsc, String field, int pageNumber, int pageSize);

    User getByEmail(String email);

}
