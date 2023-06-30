package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.entities.TeachingExperience;
import com.cloud.secure.streaming.repositories.TeachingExperienceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 689Cloud
 */
@Service
public class TeachingExperienceServiceImpl implements TeachingExperienceService{

    final TeachingExperienceRepository teachingExperienceRepository;

    public TeachingExperienceServiceImpl(TeachingExperienceRepository teachingExperienceRepository) {
        this.teachingExperienceRepository = teachingExperienceRepository;
    }

    @Override
    public TeachingExperience save(TeachingExperience teachingExperience) {
        return teachingExperienceRepository.save(teachingExperience);
    }

    @Override
    public void delete(TeachingExperience teachingExperience) {
        teachingExperienceRepository.delete(teachingExperience);
    }

    @Override
    public TeachingExperience getById(String id) {
        return teachingExperienceRepository.findById(id).orElse(null);
    }

    @Override
    public List<TeachingExperience> getAllByJobExperienceIdOrderByCreatedDateDesc(String jobId) {
        return teachingExperienceRepository.findAllByJobExperienceIdOrderByCreatedDateDesc(jobId);
    }


    @Override
    public void deleteTeachingExperienceByIdIn(List<String> teachingExperienceIds) {
        teachingExperienceRepository.deleteTeachingExperienceByIdIn(teachingExperienceIds);
    }

    @Override
    public void deleteTeachingExperienceByIdInAndUserId(List<String> teachingExperienceIds, String userId) {
        teachingExperienceRepository.deleteTeachingExperienceByIdInAndUserId(teachingExperienceIds,userId);
    }

    @Override
    public void saveAll(List<TeachingExperience> teachingExperiences) {
        teachingExperienceRepository.saveAll(teachingExperiences);
    }

    @Override
    public List<TeachingExperience> getAllByIdIn(List<String> ids) {
        return teachingExperienceRepository.findAllByIdIn(ids);
    }

    @Override
    public void deleteAllByJobExperienceId(String jobExperienceId) {
        teachingExperienceRepository.deleteAllByJobExperienceId(jobExperienceId);
    }


}
