package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.entities.Lecture;

import java.util.List;

/**
 * @author 689Cloud
 */
public interface LectureService {

    Lecture save(Lecture lecture);

    void delete(Lecture lecture);

    Lecture getById(String id);

    Lecture getByIdAndUserId(String id, String userId);

    List<Lecture> getAllBySectionIdOrderByCreatedDateDesc(String sectionId);

    List<Lecture> getAllByIdIn(List<String> ids);

    void deleteByIdIn(List<String> ids);

    void saveAll(List<Lecture> lectures);

    void deleteAllBySectionIdIn(List<String> ids);

    List<Lecture> getAllBySectionIdIn(List<String> sectionIds);

    List<Lecture> getAllByCourseId(String courseId);

    List<Lecture> getAllBySectionId(String sectionId);
}
