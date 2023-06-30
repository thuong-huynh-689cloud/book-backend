package com.cloud.secure.streaming.repositories;

import com.cloud.secure.streaming.entities.Section;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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
public interface SectionRepository extends JpaRepository<Section, String> {

    @Query("select s from Section s where s.name like :searchKey")
    Page<Section> getPageSection(@Param("searchKey") String searchKey,
                                   Pageable pageable);

    List<Section> findAllByIdIn(List<String> ids);

    void deleteAllByIdIn(List<String> ids);

    List<Section> findAllByCourseIdOrderByCreatedDateDesc(String courseId);


}
