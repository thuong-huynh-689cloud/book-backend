package com.cloud.secure.streaming.controllers.model.response;


import com.cloud.secure.streaming.entities.JobExperience;
import com.cloud.secure.streaming.entities.TeachingExperience;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class JobExperienceAndTeachingExperienceResponse {
    private String userId;
    private String jobExperienceId;
    private String position;
    private Date createdDate;
    private List<TeachingExperience> teachingExperiences;

    public JobExperienceAndTeachingExperienceResponse(JobExperience jobExperience, List<TeachingExperience> teachingExperiences) {
        this.jobExperienceId = jobExperience.getId();
        this.position = jobExperience.getPosition();
        this.userId = jobExperience.getUserId();
        this.createdDate = jobExperience.getCreatedDate();
        this.teachingExperiences = teachingExperiences;
    }
}
