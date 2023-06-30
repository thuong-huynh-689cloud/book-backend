package com.cloud.secure.streaming.controllers.helper;

import com.cloud.secure.streaming.common.utilities.DateUtil;
import com.cloud.secure.streaming.common.utilities.UniqueID;
import com.cloud.secure.streaming.controllers.model.request.CreateSectionRequest;
import com.cloud.secure.streaming.controllers.model.request.UpdateSectionRequest;
import com.cloud.secure.streaming.entities.Section;
import org.springframework.stereotype.Component;

import java.util.Date;
/**
 * @author 689Cloud
 */
@Component
public class SectionHelper {

    /**
     * createSection
     *
     * @param createSectionRequest
     * @return
     */
    public Section createSection(String courseId,CreateSectionRequest createSectionRequest) {
        Section section = new Section();
        //set Id
        section.setId(UniqueID.getUUID());
        //set name
        section.setName(createSectionRequest.getName());
        //set course id
        section.setCourseId(courseId);
        //set display status
        section.setDisplayStatus(createSectionRequest.getDisplayStatus());
        //set created date
        section.setCreatedDate(DateUtil.convertToUTC(new Date()));

        return section;
    }

    /**
     *
     * updateSection
     * @param section
     * @param updateSectionRequest
     * @return
     */
    public Section updateSection(Section section, UpdateSectionRequest updateSectionRequest
    ) {
        //check name
        if (updateSectionRequest.getName() != null && !updateSectionRequest.getName().trim().isEmpty() &&
                !updateSectionRequest.getName().trim().equals(section.getName())) {
            section.setName(updateSectionRequest.getName().trim());
        }
        // check DisplayStatus
        if (updateSectionRequest.getDisplayStatus() != null) {
            section.setDisplayStatus(updateSectionRequest.getDisplayStatus());
        }
        return section;
    }
}
