package com.cloud.secure.streaming.controllers.model.response;

import com.cloud.secure.streaming.common.enums.DisplayStatus;
import com.cloud.secure.streaming.entities.Lecture;
import com.cloud.secure.streaming.entities.Section;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SectionResponse {
    private String id;
    private String name;
    private String courseId;
    private DisplayStatus displayStatus;
    private Date createdDate;
    private List<LectureResponse> lectureResponses;

    public SectionResponse(Section section, List<LectureResponse> lectureResponses) {
        this.id = section.getId();
        this.name = section.getName();
        this.courseId = section.getCourseId();
        this.displayStatus = section.getDisplayStatus();
        this.createdDate = section.getCreatedDate();
        this.lectureResponses = lectureResponses;
    }
}
