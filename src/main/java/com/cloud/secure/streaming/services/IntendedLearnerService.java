package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.common.enums.IntendedLearnerType;
import com.cloud.secure.streaming.entities.IntendedLearner;

import java.util.List;

/**
 * @author 689Cloud
 */
public interface IntendedLearnerService {

    IntendedLearner save(IntendedLearner intendedLearner);

    void delete(IntendedLearner intendedLearner);

    IntendedLearner getById(String id);

    List<IntendedLearner> getAllIntendedLearner();

    void deleteIntendedLearnerByIdIn(List<String> intendedLearnerId);

    List<IntendedLearner> getAllBySequenceBetween(int first, int last);

    void saveAll(List<IntendedLearner> intendedLearner);

    List<IntendedLearner> getAllByIdIn(List<String> ids);

    List<IntendedLearner> getAllByUserId(String userId);

    void deleteByIdIn(List<String> ids, String courseId);

    List<IntendedLearner> getAllByCourseIdAndTypeOrderByCreatedDateDesc(String courseId, List<IntendedLearnerType> intendedLearnerTypes);

    List<IntendedLearner> getAllByCourseId(String courseId);

    List<IntendedLearner> getAllByCourseIdAndType(String courseId, IntendedLearnerType type);

    List<IntendedLearner> getAllByCourseIdIn(List<String> courseIds);
}
