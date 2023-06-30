package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.entities.UserAdminFeedback;

public interface UserAdminFeedbackService {

    UserAdminFeedback save(UserAdminFeedback userAdminFeedback);

    void delete(UserAdminFeedback userAdminFeedback);

    UserAdminFeedback getById(String id);

    void deleteAllByUserId(String userId);
}
