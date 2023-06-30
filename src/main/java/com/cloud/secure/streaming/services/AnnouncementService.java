package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.common.enums.SortDirection;
import com.cloud.secure.streaming.common.enums.SortFieldAnnouncement;
import com.cloud.secure.streaming.entities.Announcement;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AnnouncementService {
    void save(Announcement announcement);

    Announcement getById(String id);

    Page<Announcement> getPage(String courseId,
                               String searchKey,
                               SortFieldAnnouncement sortFieldAnnouncement,
                               SortDirection sortDirection,
                               int pageNumber, int pageSize);

    List<Announcement> getAllByIdIn(List<String> ids);

    void deleteByIdIn(List<String> ids);

    List<Announcement> getAllByCourseIdOrderByCreatedDateDesc(String courseId);
}
