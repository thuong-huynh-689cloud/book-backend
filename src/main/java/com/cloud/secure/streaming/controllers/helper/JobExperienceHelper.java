package com.cloud.secure.streaming.controllers.helper;

import com.cloud.secure.streaming.common.utilities.DateUtil;
import com.cloud.secure.streaming.common.utilities.UniqueID;
import com.cloud.secure.streaming.controllers.model.request.CreateJobExperiencesRequest;
import com.cloud.secure.streaming.controllers.model.request.UpdateJobExperienceRequest;
import com.cloud.secure.streaming.entities.JobExperience;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author 689Cloud
 */
@Component
public class JobExperienceHelper {

    /**
     *  Create Job Experience
     *
     * @param createJobExperiencesRequest
     * @return
     */
    public JobExperience createJobExperience(String userId ,CreateJobExperiencesRequest createJobExperiencesRequest) {

        JobExperience jobExperience = new JobExperience();
        jobExperience.setId(UniqueID.getUUID());
        jobExperience.setPosition(createJobExperiencesRequest.getPosition());
        jobExperience.setUserId(userId);
        jobExperience.setCreatedDate(DateUtil.convertToUTC(new Date()));

        return jobExperience;
    }

    /**
     *  Update JobExperience
     *
     * @param jobExperience
     * @param updateJobExperienceRequest
     * @return
     */
    public JobExperience updateJobExperience(JobExperience jobExperience, UpdateJobExperienceRequest updateJobExperienceRequest) {

        //check position
        if (updateJobExperienceRequest.getPosition() != null && !updateJobExperienceRequest.getPosition().trim().isEmpty() &&
                !updateJobExperienceRequest.getPosition().trim().equals(jobExperience.getPosition())) {
            jobExperience.setPosition(updateJobExperienceRequest.getPosition().trim());
        }

        return jobExperience;
    }
}
