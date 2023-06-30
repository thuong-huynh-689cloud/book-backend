package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.common.enums.AppStatus;
import com.cloud.secure.streaming.common.enums.MediaInfoType;
import com.cloud.secure.streaming.entities.MediaInfo;

import java.util.List;

/**
 * @author 689Cloud
 */
public interface MediaInfoService {

    MediaInfo save(MediaInfo mediaInfo);

    void delete(MediaInfo mediaInfo);

    MediaInfo getById(String id);

    MediaInfo getByIdIn(List<String> ids);

    void deleteMediaInfoByIdIn(List<String> mediaInfoId);

    List<MediaInfo> getAllByUserIdOrderByCreatedDateDesc(String authId);

    MediaInfo getMediaInfoByCourseIdOrLectureIdAndMediaInfoType(String courseId, MediaInfoType mediaInfoType);

    List<MediaInfo> getAll ();

    MediaInfo getByIdAndStatus(String id, AppStatus status);

    MediaInfo getMediaInfoByCourseIdAndMediaInfoTypeAndStatus(String courseId, MediaInfoType type, AppStatus status);

    MediaInfo getMediaInfoByLectureIdAndMediaInfoTypeAndStatus(String id, MediaInfoType type, AppStatus status);

    MediaInfo getMediaByActiveOrPending(String id);

    List<MediaInfo> getAllByLectureIdInAndTypeAndStatus(List<String> lectureIds, MediaInfoType type, AppStatus status);

    List<MediaInfo> getMediaInfoByCourseIdAndStatus(String courseId);

    MediaInfo getMediaInfosByCourseIdAndStatus(String courseId);
}