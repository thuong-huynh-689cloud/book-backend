package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.common.enums.IntendedLearnerType;
import com.cloud.secure.streaming.entities.IntendedLearner;
import com.cloud.secure.streaming.repositories.IntendedLearnerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 689Cloud
 */
@Service
public class IntendedLearnerServiceImpl implements IntendedLearnerService {
    final IntendedLearnerRepository intendedLearnerRepository;

    public IntendedLearnerServiceImpl(IntendedLearnerRepository intendedLearnerRepository) {
        this.intendedLearnerRepository = intendedLearnerRepository;
    }

    @Override
    public IntendedLearner save(IntendedLearner intendedLearner) {
        return intendedLearnerRepository.save(intendedLearner);
    }

    @Override
    public void delete(IntendedLearner intendedLearner) {
        intendedLearnerRepository.delete(intendedLearner);
    }

    @Override
    public IntendedLearner getById(String id) {
        return intendedLearnerRepository.findById(id).orElse(null);
    }

    @Override
    public List<IntendedLearner> getAllIntendedLearner() {
        return intendedLearnerRepository.findAll();
    }

    @Override
    public void deleteIntendedLearnerByIdIn(List<String> intendedLearnerId) {
        intendedLearnerRepository.deleteIntendedLearnerByIdIn(intendedLearnerId);
    }

    @Override
    public List<IntendedLearner> getAllBySequenceBetween(int first, int last) {
        return intendedLearnerRepository.findAllBySequenceBetween(first, last);
    }

    @Override
    public void saveAll(List<IntendedLearner> intendedLearner) {
        intendedLearnerRepository.saveAll(intendedLearner);
    }

    @Override
    public List<IntendedLearner> getAllByIdIn(List<String> ids) {
        return intendedLearnerRepository.findAllByIdIn(ids);
    }

    @Override
    public List<IntendedLearner> getAllByUserId(String userId) {
        return intendedLearnerRepository.findAllByUserId(userId);
    }

    @Override
    public void deleteByIdIn(List<String> ids, String courseId) {
        intendedLearnerRepository.deleteAllByIdIn(ids, courseId);
    }

    @Override
    public List<IntendedLearner> getAllByCourseIdAndTypeOrderByCreatedDateDesc(String courseId, List<IntendedLearnerType> intendedLearnerTypes) {
        return intendedLearnerRepository.findAllByCourseIdAndTypeOrderByCreatedDateDesc(courseId, intendedLearnerTypes);
    }

    @Override
    public List<IntendedLearner> getAllByCourseId(String courseId) {
        return intendedLearnerRepository.findAllByCourseId(courseId, IntendedLearnerType.PREVIEW);
    }

    @Override
    public List<IntendedLearner> getAllByCourseIdAndType(String courseId, IntendedLearnerType type) {
        return intendedLearnerRepository.findAllByCourseIdAndType(courseId, type);
    }

    @Override
    public List<IntendedLearner> getAllByCourseIdIn(List<String> courseIds) {
        return intendedLearnerRepository.findAllByCourseIdIn(courseIds);
    }
}
