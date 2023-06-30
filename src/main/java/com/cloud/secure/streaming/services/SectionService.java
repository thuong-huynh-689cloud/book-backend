package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.common.enums.DisplayStatus;
import com.cloud.secure.streaming.common.enums.SortDirection;
import com.cloud.secure.streaming.common.enums.SortFieldSection;
import com.cloud.secure.streaming.entities.Section;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author 689Cloud
 */
public interface SectionService {

    Section save(Section section);

    void delete(Section section);

    Section getById(String id);

    Page<Section> getPage(String searchKey,
                           SortFieldSection sortFieldSection,
                           SortDirection sortDirection, List<DisplayStatus> displayStatuses,
                           int pageNumber, int pageSize);

    List<Section> getAllByIdIn(List<String> ids);

    void deleteByIdIn(List<String> ids);

    List<Section> getAll();

    void saveAll(List<Section> sections);

    List<Section> getAllByCourseIdOrderByCreatedDateDesc(String courseId);
}
