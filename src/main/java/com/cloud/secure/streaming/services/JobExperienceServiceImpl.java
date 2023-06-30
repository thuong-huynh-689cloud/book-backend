package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.entities.JobExperience;
import com.cloud.secure.streaming.repositories.JobExperienceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 689Cloud
 */
@Service
public class JobExperienceServiceImpl implements JobExperienceService {

    final JobExperienceRepository jobExperienceRepository;

    public JobExperienceServiceImpl(JobExperienceRepository jobExperienceRepository) {
        this.jobExperienceRepository = jobExperienceRepository;
    }

    @Override
    public JobExperience save(JobExperience jobExperience) {
        return jobExperienceRepository.save(jobExperience);
    }

    @Override
    public void delete(JobExperience jobExperience) {
        jobExperienceRepository.delete(jobExperience);
    }

    @Override
    public JobExperience getById(String id) {
        return jobExperienceRepository.findById(id).orElse(null);
    }

    @Override
    public void saveAll(List<JobExperience> jobExperiences) {
        jobExperienceRepository.saveAll(jobExperiences);
    }

    @Override
    public List<JobExperience> getAllByUserIdOrderByCreatedDateDesc(String userId) {
        return jobExperienceRepository.findAllByUserIdOrderByCreatedDateDesc(userId);
    }

    @Override
    public void  deleteByUserId(List<String> jobExperienceIds,String userId) {
         jobExperienceRepository.deleteByUserId(jobExperienceIds,userId);
    }

    @Override
    public void deleteByIdIn(List<String> jobExperienceIds) {
        jobExperienceRepository.deleteByIdIn(jobExperienceIds);
    }

    @Override
    public List<JobExperience> getAllByIdIn(List<String> ids) {
        return jobExperienceRepository.findAllByIdIn(ids);
    }

    @Override
    public List<JobExperience> getAllByUserId(String userId) {
        return jobExperienceRepository.findAllByUserId(userId);
    }
}
