package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.entities.TeachingExperience;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author 689Cloud
 */
public interface TeachingExperienceService {

    TeachingExperience save(TeachingExperience teachingExperience);

    void delete(TeachingExperience teachingExperience);

    TeachingExperience getById(String id);

    List<TeachingExperience> getAllByJobExperienceIdOrderByCreatedDateDesc(String jobId);

    void deleteTeachingExperienceByIdIn(List<String> teachingExperienceIds);

    void deleteTeachingExperienceByIdInAndUserId(List<String> teachingExperienceIds ,String userId);

    void saveAll(List<TeachingExperience> teachingExperiences);

    List<TeachingExperience> getAllByIdIn(List<String> ids);

    void deleteAllByJobExperienceId(String jobExperienceId);
}
