package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.entities.JobExperience;

import java.util.List;

/**
 * @author 689Cloud
 */
public interface JobExperienceService {

    JobExperience save(JobExperience jobExperience);

    void delete(JobExperience jobExperience);

    JobExperience getById(String id);

    void saveAll(List<JobExperience> jobExperiences);

    List<JobExperience> getAllByUserIdOrderByCreatedDateDesc(String userId);

    void deleteByUserId(List<String> jobExperienceIds,String userId);

    void deleteByIdIn(List<String> jobExperienceIds);

    List<JobExperience> getAllByIdIn(List<String> ids);

    List<JobExperience> getAllByUserId(String userId);
}
