package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.common.enums.AppStatus;
import com.cloud.secure.streaming.common.enums.UserType;
import com.cloud.secure.streaming.entities.User;
/**
 * @author 689Cloud
 */
public interface UserService {

    User save(User user);

    void delete(User user);

    User getById(String id);

    User getByEmailAndStatusAndType(String email, AppStatus status, UserType userType);
}
