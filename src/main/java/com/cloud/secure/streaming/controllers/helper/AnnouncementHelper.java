package com.cloud.secure.streaming.controllers.helper;

import com.cloud.secure.streaming.common.utilities.DateUtil;
import com.cloud.secure.streaming.common.utilities.UniqueID;
import com.cloud.secure.streaming.controllers.model.request.CreateAnnouncementRequest;
import com.cloud.secure.streaming.controllers.model.request.UpdateAnnouncementRequest;
import com.cloud.secure.streaming.entities.Announcement;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class AnnouncementHelper {
    /**
     * Create Announcement
     *
     * @param id
     * @param createAnnouncementRequest
     * @return
     */
    public Announcement createAnnouncement(String id, CreateAnnouncementRequest createAnnouncementRequest) {
        Announcement announcement = new Announcement(
                UniqueID.getUUID(), // set id
                createAnnouncementRequest.getContent(), // set content
                createAnnouncementRequest.getCourseId(), // set course id
                id // set user id
        );
        // set created date
        announcement.setCreatedDate(DateUtil.convertToUTC(new Date()));

        return announcement;
    }

    /**
     * Create Announcement
     *
     * @param announcement
     * @param updateAnnouncementRequest
     * @return
     */
    public Announcement updateAnnouncement(Announcement announcement, UpdateAnnouncementRequest updateAnnouncementRequest) {
        // check content
        if (updateAnnouncementRequest.getContent() != null && !updateAnnouncementRequest.getContent().trim().isEmpty()
                && !updateAnnouncementRequest.getContent().trim().equals(announcement.getContent())) {
            announcement.setContent(updateAnnouncementRequest.getContent());
        }
        return announcement;
    }
}
