package com.cloud.secure.streaming.controllers.helper;

import com.cloud.secure.streaming.common.utilities.DateUtil;
import com.cloud.secure.streaming.common.utilities.UniqueID;
import com.cloud.secure.streaming.entities.UserAdminFeedback;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author 689Cloud
 */
@Component
public class UserAdminFeedbackHelper {

    /**
     * Create User Admin Feedback
     * @param userId
     * @param feedback
     * @return
     */
    public UserAdminFeedback createUserAdminFeedback(String userId, String feedback){
        UserAdminFeedback userAdminFeedback = new UserAdminFeedback();
        //add id to data
        userAdminFeedback.setId(UniqueID.getUUID());
        // add courseId to data
        userAdminFeedback.setUserId(userId);
        // add title to data
        userAdminFeedback.setFeedback(feedback);
        // add createDate to date
        userAdminFeedback.setCreatedDate(DateUtil.convertToUTC(new Date()));

        return userAdminFeedback;
    }
}
