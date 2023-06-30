package com.cloud.secure.streaming.controllers.model.response;

import com.cloud.secure.streaming.common.enums.*;
import com.cloud.secure.streaming.entities.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrolCourseResponse {
    private String id;
    private String userId;
    private String name;
    private String title;
    private CourseStatus courseStatus; // WAITING_FOR_APPROVAL,PUBLISH,DRAFT,REJECT
    private double point = 0;
    private double totalUserEnrol;
//    private double discount = 0;
//    private String image;
//    private Date expireDate;
//    private Date createdDate;




    public EnrolCourseResponse(String id, String userId, String name, String title, String point, String courseStatus,
                               String totalUserEnrol ) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.title = title;
        this.courseStatus = CourseStatus.valueOf(courseStatus);
        this.point = Double.parseDouble(point);
        this.totalUserEnrol = Double.parseDouble(totalUserEnrol);
//        this.discount = Double.parseDouble(discount);
//        this.image = image;

    }


//    public EnrolCourseResponse(Course course, User user, long totalUserEnrol) {
//        this.id = course.getId();
//        this.userId = user.getId();
//        this.name = user.getName();
//        this.title = course.getTitle();
//        this.courseStatus = course.getCourseStatus();
//        this.point = course.getPoint();
//        this.totalUserEnrol = (totalUserEnrol);
//    }
}