package com.cloud.secure.streaming.controllers.model.response;

import com.cloud.secure.streaming.common.enums.ContentType;
import com.cloud.secure.streaming.entities.Lecture;
import com.cloud.secure.streaming.entities.MediaInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LectureResponse {

    private String id;
    private String lectureName;
    private ContentType contentType;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String fileName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String originalFileName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long fileSize;
    private Integer duration;
    private Integer numberOfQuestion;
    private Integer numberOfPage;
    private boolean downloadable;
    private boolean publicView;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long videoDuration = null;
    private String sectionId;

    public LectureResponse(Lecture lecture, MediaInfo mediaInfo){
        this.id = lecture.getId();
        this.lectureName = lecture.getLectureName();
        this.contentType = lecture.getContentType();
        this.fileName = lecture.getFileName();
        this.originalFileName = lecture.getOriginalFileName();
        this.fileSize = lecture.getFileSize();
        this.duration = lecture.getDuration();
        this.numberOfQuestion = lecture.getNumberOfQuestion();
        this.downloadable = lecture.isDownloadable();
        this.publicView = lecture.isPublicView();
        if(mediaInfo != null) {
            this.videoDuration = mediaInfo.getDuration();
        }
        this.sectionId = lecture.getSectionId();
    }

    public LectureResponse(Lecture lecture){
        this.id = lecture.getId();
        this.lectureName = lecture.getLectureName();
        this.contentType = lecture.getContentType();
        this.fileName = lecture.getFileName();
        this.duration = lecture.getDuration();
        this.numberOfQuestion = lecture.getNumberOfQuestion();
        this.downloadable = lecture.isDownloadable();
        this.publicView = lecture.isPublicView();
        this.sectionId = lecture.getSectionId();
    }
}
