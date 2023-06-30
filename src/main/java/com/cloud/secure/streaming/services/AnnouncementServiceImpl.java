package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.common.enums.SortDirection;
import com.cloud.secure.streaming.common.enums.SortFieldAnnouncement;
import com.cloud.secure.streaming.entities.Announcement;
import com.cloud.secure.streaming.repositories.AnnouncementRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {
    final AnnouncementRepository announcementRepository;

    public AnnouncementServiceImpl(AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }

    @Override
    public void save(Announcement announcement) {
        announcementRepository.save(announcement);
    }

    @Override
    public Announcement getById(String id) {
        return announcementRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Announcement> getPage(String courseId, String searchKey, SortFieldAnnouncement sortFieldAnnouncement, SortDirection sortDirection, int pageNumber, int pageSize) {
        Sort.Direction direction = Sort.Direction.ASC;
        if (sortDirection.equals(SortDirection.DESC)) {
            direction = Sort.Direction.DESC;
        }
        Sort sort = Sort.by(direction, sortFieldAnnouncement.toString());
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        return announcementRepository.getPageAnnouncement(courseId, "%" + searchKey + "%", pageable);
    }

    @Override
    public List<Announcement> getAllByIdIn(List<String> ids) {
        return announcementRepository.findAllByIdIn(ids);
    }

    @Override
    public void deleteByIdIn(List<String> ids) {
        announcementRepository.deleteAllByIdIn(ids);
    }

    @Override
    public List<Announcement> getAllByCourseIdOrderByCreatedDateDesc(String courseId) {
        return announcementRepository.findAllByCourseIdOrderByCreatedDateDesc(courseId);
    }
}
