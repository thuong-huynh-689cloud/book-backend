package com.cloud.secure.streaming.repositories;

import com.cloud.secure.streaming.common.enums.AppStatus;
import com.cloud.secure.streaming.common.enums.MediaInfoType;
import com.cloud.secure.streaming.entities.MediaInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 689Cloud
 */
@Repository
public interface MediaInfoRepository extends JpaRepository<MediaInfo, String> {

    @Modifying
    @Transactional
    @Query("DELETE from MediaInfo m where m.id in :ids")
    void deleteMediaInfoByIdIn(@Param("ids") List<String> mediaInfoId);

    MediaInfo findByIdIn(List<String> isd);

    List<MediaInfo> findAllByUserIdOrderByCreatedDateDesc(String authId);

    @Query("select m from MediaInfo m where m.mediaInfoType =:mediaInfoType and (m.courseId = :id or m.lectureId = :id)")
    MediaInfo findMediaInfoByCourseIdOrLectureIdAndMediaInfoType(@Param("id") String id, @Param("mediaInfoType") MediaInfoType mediaInfoType);

    MediaInfo findByIdAndStatus(String id, AppStatus status);

    @Query("select m from MediaInfo m where m.courseId = :id and m.mediaInfoType = :type and m.status = :status")
    MediaInfo findMediaInfoByCourseIdAndMediaInfoTypeAndStatus(@Param("id") String id,
                                                      @Param("type") MediaInfoType type,
                                                      @Param("status") AppStatus status);

    @Query("select m from MediaInfo m where m.lectureId = :id and m.mediaInfoType = :type and m.status = :status")
    MediaInfo findMediaInfoByLectureIdAndMediaInfoTypeAndStatus(@Param("id") String id,
                                              @Param("type") MediaInfoType type,
                                              @Param("status") AppStatus status);

    MediaInfo findByIdAndStatusIn(String id ,List<AppStatus> appStatus);

    List<MediaInfo> findAllByLectureIdInAndMediaInfoTypeAndStatus(List<String> lectureIds, MediaInfoType type, AppStatus status);

    List<MediaInfo> findMediaInfoByCourseIdAndStatus(String courseId,AppStatus appStatus);

    MediaInfo findMediaInfosByCourseIdAndStatus(String courseId,AppStatus appStatus);
}
