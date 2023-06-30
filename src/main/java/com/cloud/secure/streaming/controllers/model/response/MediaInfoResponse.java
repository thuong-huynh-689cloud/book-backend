package com.cloud.secure.streaming.controllers.model.response;

import com.cloud.secure.streaming.common.enums.AppStatus;
import com.cloud.secure.streaming.common.enums.MediaInfoType;
import com.cloud.secure.streaming.common.enums.VideoType;
import com.cloud.secure.streaming.entities.MediaInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MediaInfoResponse {

    private String id;
    private String name;
    private String nameOriginal;
    private String extension;
    private Long fileSize; // By bytes
    private Long duration; // By seconds
    private String userId;
    private String lectureId;
    private String courseId;
    private MediaInfoType mediaInfoType;
    private VideoType videoType;
    private AppStatus status;
    private Boolean isEncrypt;
    private String url;

    public MediaInfoResponse(MediaInfo mediaInfo, String url) {
        this.id = mediaInfo.getId();
        this.name = mediaInfo.getName();
        this.nameOriginal = mediaInfo.getNameOriginal();
        this.extension = mediaInfo.getExtension();
        this.fileSize = mediaInfo.getFileSize();
        this.duration = mediaInfo.getDuration();
        this.userId = mediaInfo.getUserId();
        this.lectureId = mediaInfo.getLectureId();
        this.courseId = mediaInfo.getCourseId();
        this.mediaInfoType = mediaInfo.getMediaInfoType();
        this.videoType = mediaInfo.getVideoType();
        this.status = mediaInfo.getStatus();
        this.isEncrypt = mediaInfo.isEncrypt();
        this.url = url;
    }
}
