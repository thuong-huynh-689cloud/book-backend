package com.cloud.secure.streaming.controllers.helper;

import com.cloud.secure.streaming.common.utilities.DateUtil;
import com.cloud.secure.streaming.common.utilities.UniqueID;
import com.cloud.secure.streaming.controllers.model.request.CreateLectureRequest;
import com.cloud.secure.streaming.controllers.model.request.UpdateLectureRequest;
import com.cloud.secure.streaming.entities.Lecture;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author 689Cloud
 */
@Component
public class LectureHelper {

    /**
     * Create Lecture
     *
     * @param sectionId
     * @param createLectureRequest
     * @return
     */
    public Lecture createLecture(String sectionId, CreateLectureRequest createLectureRequest) {
        Lecture lecture = new Lecture();
        //set Id
        lecture.setId(UniqueID.getUUID());
        // set section Id
        lecture.setSectionId(sectionId);
        //set lecture name
        lecture.setLectureName(createLectureRequest.getLectureName());
        //set content type
        lecture.setContentType(createLectureRequest.getContentType());
        //set file name
        lecture.setFileName(createLectureRequest.getFileName());
        //set original file name
        lecture.setOriginalFileName(createLectureRequest.getOriginalFileName());
        //set file size
        lecture.setFileSize(createLectureRequest.getFileSize());
        //set duration
        lecture.setDuration(createLectureRequest.getDuration());
        //set number of question
        lecture.setNumberOfQuestion(createLectureRequest.getNumberOfQuestion());
        // set downloadable
        lecture.setDownloadable(createLectureRequest.getDownloadable());
        //set public view
        lecture.setPublicView(createLectureRequest.getPublicView());
        // set created date
        lecture.setCreatedDate(DateUtil.convertToUTC(new Date()));

        return lecture;
    }

    /**
     * Update Lecture
     *
     * @param lecture
     * @param updateLectureRequest
     * @return
     */
    public Lecture updateLecture(Lecture lecture, UpdateLectureRequest updateLectureRequest, int duration) {
        //check lecture name
        if (updateLectureRequest.getLectureName() != null && !updateLectureRequest.getLectureName().trim().isEmpty() &&
                !updateLectureRequest.getLectureName().trim().equals(lecture.getLectureName())) {
            lecture.setLectureName(updateLectureRequest.getLectureName().trim());
        }
        //check content type
        if (updateLectureRequest.getContentType() != null &&
                !updateLectureRequest.getContentType().equals(lecture.getContentType())) {
            lecture.setContentType(updateLectureRequest.getContentType());
        }
        //check file name
        if (updateLectureRequest.getFileName() != null && !updateLectureRequest.getFileName().trim().isEmpty() &&
                !updateLectureRequest.getFileName().trim().equals(lecture.getFileName())) {
            lecture.setFileName(updateLectureRequest.getFileName().trim());
        }
        //check original file name
        if (updateLectureRequest.getOriginalFileName() != null && !updateLectureRequest.getOriginalFileName().trim().isEmpty() &&
                !updateLectureRequest.getOriginalFileName().trim().equals(lecture.getOriginalFileName())) {
            lecture.setOriginalFileName(updateLectureRequest.getOriginalFileName().trim());
        }
        //update file size
        if (updateLectureRequest.getFileSize() != null && !updateLectureRequest.getFileSize().equals(lecture.getFileSize())) {
            lecture.setFileSize(updateLectureRequest.getFileSize());
        }
        //check file path
        if (updateLectureRequest.getFileName() != null && !updateLectureRequest.getFileName().trim().isEmpty() &&
                !updateLectureRequest.getFileName().trim().equals(lecture.getFileName())) {
            lecture.setFileName(updateLectureRequest.getFileName().trim());
        }
        //update duration
//        if (updateLectureRequest.getDuration() != null && !updateLectureRequest.getDuration().equals(lecture.getDuration())){
            lecture.setDuration(duration);
//        }
        // Update number of question
        if (updateLectureRequest.getNumberOfQuestion() != null && !updateLectureRequest.getNumberOfQuestion().equals(lecture.getNumberOfQuestion())){
            lecture.setNumberOfQuestion(updateLectureRequest.getNumberOfQuestion());
        }
        // check downloadable
        if (updateLectureRequest.getDownloadable() != null &&
                !updateLectureRequest.getDownloadable().equals(lecture.isDownloadable())) {
            lecture.setDownloadable(updateLectureRequest.getDownloadable());
        }
        // check public view
        if (updateLectureRequest.getPublicView() != null &&
                !updateLectureRequest.getPublicView().equals(lecture.isPublicView())) {
            lecture.setPublicView(updateLectureRequest.getPublicView());
        }
        return lecture;
    }
}
