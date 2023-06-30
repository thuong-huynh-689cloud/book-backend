package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.common.enums.AppStatus;
import com.cloud.secure.streaming.common.enums.MediaInfoType;
import com.cloud.secure.streaming.entities.MediaInfo;
import com.cloud.secure.streaming.repositories.MediaInfoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 689Cloud
 */
@Service
public class MediaInfoServiceImpl implements MediaInfoService {
    final MediaInfoRepository mediaInfoRepository;

    public MediaInfoServiceImpl(MediaInfoRepository mediaInfoRepository) {
        this.mediaInfoRepository = mediaInfoRepository;
    }

    @Override
    public MediaInfo save(MediaInfo mediaInfo) {
        return mediaInfoRepository.save(mediaInfo);
    }

    @Override
    public void delete(MediaInfo mediaInfo) {
        mediaInfoRepository.delete(mediaInfo);
    }

    @Override
    public MediaInfo getById(String id) {
        return mediaInfoRepository.findById(id).orElse(null);
    }

    @Override
    public MediaInfo getByIdIn(List<String> ids) {
        return mediaInfoRepository.findByIdIn(ids);
    }

    @Override
    public void deleteMediaInfoByIdIn(List<String> mediaInfoId) {
        mediaInfoRepository.deleteMediaInfoByIdIn(mediaInfoId);
    }

    @Override
    public List<MediaInfo> getAllByUserIdOrderByCreatedDateDesc(String authId) {
        return mediaInfoRepository.findAllByUserIdOrderByCreatedDateDesc(authId);
    }

    @Override
    public MediaInfo getMediaInfoByCourseIdOrLectureIdAndMediaInfoType(String courseId, MediaInfoType mediaInfoType) {
        return mediaInfoRepository.findMediaInfoByCourseIdOrLectureIdAndMediaInfoType(courseId, mediaInfoType);
    }

    @Override
    public List<MediaInfo> getAll() {
        return mediaInfoRepository.findAll();
    }

    @Override
    public MediaInfo getByIdAndStatus(String id, AppStatus status) {
        return mediaInfoRepository.findByIdAndStatus(id, status);
    }

    @Override
    public MediaInfo getMediaInfoByCourseIdAndMediaInfoTypeAndStatus(String courseId, MediaInfoType type, AppStatus status) {
        return mediaInfoRepository.findMediaInfoByCourseIdAndMediaInfoTypeAndStatus(courseId, type, status);
    }

    @Override
    public MediaInfo getMediaInfoByLectureIdAndMediaInfoTypeAndStatus(String id, MediaInfoType type, AppStatus status) {
        return mediaInfoRepository.findMediaInfoByLectureIdAndMediaInfoTypeAndStatus(id, type, status);
    }

    @Override
    public MediaInfo getMediaByActiveOrPending(String id) {
        List<AppStatus> appStatusList = new ArrayList<>();
        appStatusList.add(AppStatus.ACTIVE);
        appStatusList.add(AppStatus.PENDING);
        appStatusList.add(AppStatus.ERROR);
        return mediaInfoRepository.findByIdAndStatusIn(id, appStatusList);
    }

    @Override
    public List<MediaInfo> getAllByLectureIdInAndTypeAndStatus(List<String> lectureIds, MediaInfoType type, AppStatus status) {
        return mediaInfoRepository.findAllByLectureIdInAndMediaInfoTypeAndStatus(lectureIds, type, status);
    }

    @Override
    public List<MediaInfo> getMediaInfoByCourseIdAndStatus(String courseId) {
        return mediaInfoRepository.findMediaInfoByCourseIdAndStatus(courseId, AppStatus.ACTIVE);
    }

    @Override
    public MediaInfo getMediaInfosByCourseIdAndStatus(String courseId) {
        return mediaInfoRepository.findMediaInfosByCourseIdAndStatus(courseId, AppStatus.ACTIVE);
    }
}
