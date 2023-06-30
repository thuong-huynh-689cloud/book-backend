package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.entities.UserAdminFeedback;
import com.cloud.secure.streaming.repositories.UserAdminFeedbackRepository;
import org.springframework.stereotype.Service;

@Service
public class UserAdminFeedbackServiceImpl implements UserAdminFeedbackService{

    final UserAdminFeedbackRepository userAdminFeedbackRepository;

    public UserAdminFeedbackServiceImpl(UserAdminFeedbackRepository userAdminFeedbackRepository) {
        this.userAdminFeedbackRepository = userAdminFeedbackRepository;
    }

    @Override
    public UserAdminFeedback save(UserAdminFeedback userAdminFeedback) {
        return userAdminFeedbackRepository.save(userAdminFeedback);
    }

    @Override
    public void delete(UserAdminFeedback userAdminFeedback) {
        userAdminFeedbackRepository.delete(userAdminFeedback);
    }

    @Override
    public UserAdminFeedback getById(String id) {
        return userAdminFeedbackRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteAllByUserId(String userId) {
        userAdminFeedbackRepository.deleteUserAdminFeedbackById(userId);
    }
}
