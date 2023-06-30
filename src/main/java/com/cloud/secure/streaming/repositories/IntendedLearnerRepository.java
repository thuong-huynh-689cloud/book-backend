package com.cloud.secure.streaming.repositories;

import com.cloud.secure.streaming.common.enums.IntendedLearnerType;
import com.cloud.secure.streaming.entities.IntendedLearner;
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
@Transactional
public interface IntendedLearnerRepository extends JpaRepository<IntendedLearner, String> {
    @Modifying
    @Query("DELETE from IntendedLearner i where i.id in :ids")
    void deleteIntendedLearnerByIdIn(@Param("ids") List<String> intendedLearnerId);

    List<IntendedLearner> findAllBySequenceBetween(int first, int last);

    List<IntendedLearner> findAllByIdIn(List<String> ids);

    void deleteAllByCourseId(String courseId);

    @Query("select i from IntendedLearner i, Course c where i.courseId = c.id and c.userId = :userId")
    List<IntendedLearner> findAllByUserId(@Param("userId") String userId);

    @Modifying
    @Query("delete from IntendedLearner i where i.id in :ids and i.courseId = :courseId")
    void deleteAllByIdIn(@Param("ids") List<String> ids,
                         @Param("courseId") String courseId);

    @Query("select i from IntendedLearner i where i.courseId = :courseId and i.type in :type order by i.createdDate asc")
    List<IntendedLearner> findAllByCourseIdAndTypeOrderByCreatedDateDesc(@Param("courseId") String courseId,
                                                   @Param("type") List<IntendedLearnerType> intendedLearnerTypes);

    @Query("select i from IntendedLearner i where i.courseId = :courseId and i.type <> :type")
    List<IntendedLearner> findAllByCourseId(@Param("courseId") String courseId,
                                            @Param("type") IntendedLearnerType type);

    List<IntendedLearner> findAllByCourseIdAndType(String courseId, IntendedLearnerType intendedLearnerTypes);

    List<IntendedLearner> findAllByCourseIdIn(List<String> courseIds);
}
