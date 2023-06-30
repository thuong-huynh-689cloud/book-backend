package com.cloud.secure.streaming.repositories;

import com.cloud.secure.streaming.entities.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ResourceRepository extends JpaRepository<Resource, String> {

    @Modifying
    @Query("DELETE from Resource r where r.id in :ids")
    void deleteResourceByIdIn(@Param("ids") List<String> resourceId);

    List<Resource> findAllByIdIn(List<String> ids);

    List<Resource> findAllByLectureIdOrderByCreatedDateDesc(String lectureId);
}
