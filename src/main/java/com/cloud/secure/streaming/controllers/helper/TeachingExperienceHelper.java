package com.cloud.secure.streaming.controllers.helper;

import com.cloud.secure.streaming.common.utilities.DateUtil;
import com.cloud.secure.streaming.common.utilities.UniqueID;
import com.cloud.secure.streaming.controllers.model.request.CreateTeachingExperienceRequest;
import com.cloud.secure.streaming.controllers.model.request.UpdateTeachingExperienceRequest;
import com.cloud.secure.streaming.entities.TeachingExperience;
import org.springframework.stereotype.Component;

import java.util.Date;
/**
 * @author 689Cloud
 */
@Component
public class TeachingExperienceHelper {

    /**
     *  Create Teaching Experience
     * @param jopExperienceId
     * @param createTeachingExperienceRequest
     * @return
     */
    public TeachingExperience createTeachingExperience(String jopExperienceId, CreateTeachingExperienceRequest createTeachingExperienceRequest) {

        TeachingExperience teachingExperience = new TeachingExperience();
        teachingExperience.setId(UniqueID.getUUID());
        teachingExperience.setJobExperienceId(jopExperienceId);
        teachingExperience.setTeachingPlace(createTeachingExperienceRequest.getTeachingPlace());
        teachingExperience.setType(createTeachingExperienceRequest.getTeachingType());
        teachingExperience.setMajor(createTeachingExperienceRequest.getMajor());
        teachingExperience.setDescription(createTeachingExperienceRequest.getDescription());
        teachingExperience.setCreatedDate(DateUtil.convertToUTC(new Date()));

        return teachingExperience;
    }

    /**
     * Update TeachingExperience
     * @param teachingExperience
     * @param updateTeachingExperienceRequest
     * @return
     */
    public TeachingExperience updateTeachingExperience(TeachingExperience teachingExperience,
                                                       UpdateTeachingExperienceRequest updateTeachingExperienceRequest
    ) {
        // check teaching place
        if (updateTeachingExperienceRequest.getTeachingPlace() != null && !updateTeachingExperienceRequest.getTeachingPlace().trim().isEmpty()
                && !updateTeachingExperienceRequest.getTeachingPlace().trim().equals(teachingExperience.getTeachingPlace())) {
            teachingExperience.setTeachingPlace(updateTeachingExperienceRequest.getTeachingPlace());
        }
        // check teaching type
        if (updateTeachingExperienceRequest.getType() != null && !updateTeachingExperienceRequest.getType().equals(teachingExperience.getType())) {
            teachingExperience.setType(updateTeachingExperienceRequest.getType());
        }
        // check major
        if (updateTeachingExperienceRequest.getMajor() != null && !updateTeachingExperienceRequest.getMajor().trim().isEmpty()
                && !updateTeachingExperienceRequest.getMajor().trim().equals(teachingExperience.getMajor())) {
            teachingExperience.setMajor(updateTeachingExperienceRequest.getMajor());
        }
        // check description
        if (updateTeachingExperienceRequest.getDescription() != null && !updateTeachingExperienceRequest.getDescription().trim().isEmpty()
                && !updateTeachingExperienceRequest.getDescription().trim().equals(teachingExperience.getDescription())) {
            teachingExperience.setDescription(updateTeachingExperienceRequest.getDescription());
        }
        return teachingExperience;
    }
}
