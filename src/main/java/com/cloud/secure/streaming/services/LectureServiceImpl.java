package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.entities.Lecture;
import com.cloud.secure.streaming.repositories.LectureRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 689Cloud
 */
@Service
public class LectureServiceImpl implements LectureService {

    final LectureRepository lectureRepository;

    public LectureServiceImpl(LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }

    @Override
    public Lecture save(Lecture lecture){
        return lectureRepository.save(lecture);
    }

    @Override
    public void delete(Lecture lecture){
        lectureRepository.delete(lecture);
    }

    @Override
    public Lecture getById(String id){
        return lectureRepository.findById(id).orElse(null);
    }

    @Override
    public Lecture getByIdAndUserId(String id, String userId) {
        return lectureRepository.findByLectureIdAndUserId(id, userId);
    }

    @Override
    public  List<Lecture> getAllBySectionIdOrderByCreatedDateDesc(String sectionId){
        return lectureRepository.findAllBySectionIdOrderByCreatedDateDesc(sectionId);
    }

    @Override
    public List<Lecture> getAllByIdIn(List<String> ids){
        return lectureRepository.findAllByIdIn(ids);
    }

    @Override
    public void deleteByIdIn(List<String> ids) {
        lectureRepository.deleteAllByIdIn(ids);
    }
    @Override
    public void saveAll(List<Lecture> lectures) {
        lectureRepository.saveAll(lectures);
    }
    @Override
    public void deleteAllBySectionIdIn(List<String> ids){
        lectureRepository.deleteAllBySectionIdIn(ids);
    }

    @Override
    public List<Lecture> getAllBySectionIdIn(List<String> sectionIds) {
        return lectureRepository.findAllBySectionIdIn(sectionIds);
    }

    @Override
    public List<Lecture> getAllByCourseId(String courseId) {
        return lectureRepository.findAllByCourseId(courseId);
    }

    @Override
    public List<Lecture> getAllBySectionId(String sectionId) {
        return lectureRepository.findAllBySectionId(sectionId);
    }

}
