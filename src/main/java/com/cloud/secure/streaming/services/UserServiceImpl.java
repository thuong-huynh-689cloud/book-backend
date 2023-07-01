package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.common.enums.AppStatus;
import com.cloud.secure.streaming.common.enums.UserType;
import com.cloud.secure.streaming.entities.User;
import com.cloud.secure.streaming.repositories.UserRepository;
import org.springframework.stereotype.Service;

/**
 * @author 689Cloud
 */
@Service
public class UserServiceImpl implements UserService {

    final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public User getById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User getByEmailAndStatusAndType(String email, AppStatus status, UserType userType) {
        return userRepository.findByEmailAndStatusAndType(email, status, userType);
    }
}
