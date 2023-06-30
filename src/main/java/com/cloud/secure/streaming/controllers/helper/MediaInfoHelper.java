package com.cloud.secure.streaming.controllers.helper;

import com.cloud.secure.streaming.common.enums.AppStatus;
import com.cloud.secure.streaming.common.enums.MediaInfoType;
import com.cloud.secure.streaming.common.enums.VideoType;
import com.cloud.secure.streaming.common.utilities.DateUtil;
import com.cloud.secure.streaming.entities.MediaInfo;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author 689Cloud
 */
@Component
public class MediaInfoHelper {

    /**
     *  Create MediaInfo API
     *
     * @param name
     * @param userId
     * @param extension
     * @param nameOriginal
     * @param lectureId
     * @param courseId
     * @param appStatus
     * @param videoType
     * @param encrypt
     * @return
     */
    public MediaInfo createMediaInfo(String name, String userId, String extension, String nameOriginal, String lectureId, String courseId,AppStatus appStatus,VideoType videoType,boolean encrypt) {

        MediaInfo mediaInfo = new MediaInfo();
        mediaInfo.setId(name);
        mediaInfo.setName(name);
        mediaInfo.setExtension(extension);
        mediaInfo.setFileSize(0L);
        mediaInfo.setDuration(0L);
        mediaInfo.setUserId(userId);
        mediaInfo.setNameOriginal(nameOriginal);
        if (lectureId != null && !lectureId.isEmpty()) {
            mediaInfo.setLectureId(lectureId);
            mediaInfo.setMediaInfoType(MediaInfoType.LECTURE);
        }
        if (courseId != null && !courseId.isEmpty()) {
            mediaInfo.setCourseId(courseId);
            mediaInfo.setMediaInfoType(MediaInfoType.COURSE);
        }
        mediaInfo.setStatus(appStatus);

        mediaInfo.setVideoType(videoType);

        mediaInfo.setEncrypt(encrypt);

        mediaInfo.setCreatedDate(DateUtil.convertToUTC(new Date()));

        return mediaInfo;
    }
}
