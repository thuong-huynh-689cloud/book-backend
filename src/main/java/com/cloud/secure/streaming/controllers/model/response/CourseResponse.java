package com.cloud.secure.streaming.controllers.model.response;

import com.cloud.secure.streaming.common.enums.*;
import com.cloud.secure.streaming.common.utilities.Constant;
import com.cloud.secure.streaming.entities.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseResponse {
    private String id;
    private String userId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;
    private String title;
    private String subtitle;
    private CourseStatus courseStatus; // WAITING_FOR_APPROVAL,PUBLISH,DRAFT,REJECT
    private String image;
    private String promotionVideo;
    private String description;
    private Language language;
    private double point = 0;
    private double discount = 0;
    private Date expireDate;
    private AppStatus status;
    private CourseLevel level;
    private String courseInformationType;
    private CoursePrice coursePrice;
    private boolean showPromotion;
    private Date createdDate;


    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Category> category;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<IntendedLearner> intendedLearners;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private CourseAdminFeedback courseAdminFeedback;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long totalDuration = null;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private double totalUserEnrol;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private MediaInfo mediaInfo;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean payment;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private OrderStatus orderStatus;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private double totalPoint;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<OrderDetail> orderDetails;

    public CourseResponse(Course course, User user, List<Category> category, CourseAdminFeedback courseAdminFeedback,boolean payment) {
        this.id = course.getId();
        this.userId = course.getUserId();
        this.name = user.getName();
        this.title = course.getTitle();
        this.subtitle = course.getSubtitle();
        this.courseStatus = course.getCourseStatus();
        this.image = course.getImage();
        this.promotionVideo = course.getPromotionVideo();
        this.description = course.getDescription();
        this.language = course.getLanguage();
        this.point = course.getPoint();
        this.discount = course.getDiscount();
        this.expireDate = course.getExpireDate();
        this.status = course.getStatus();
        this.level = course.getLevel();
        this.courseInformationType = course.getCourseInformationType();
        this.coursePrice = course.getCoursePrice();
        this.showPromotion = course.isShowPromotion();
        this.createdDate = course.getCreatedDate();
        this.category = category;
        this.courseAdminFeedback = courseAdminFeedback;
        this.payment = payment;
    }

    public CourseResponse(Course course, User user, List<Category> category, CourseAdminFeedback courseAdminFeedback,MediaInfo mediaInfo) {
        this.id = course.getId();
        this.userId = course.getUserId();
        this.name = user.getName();
        this.title = course.getTitle();
        this.subtitle = course.getSubtitle();
        this.courseStatus = course.getCourseStatus();
        this.image = course.getImage();
        this.promotionVideo = course.getPromotionVideo();
        this.description = course.getDescription();
        this.language = course.getLanguage();
        this.point = course.getPoint();
        this.discount = course.getDiscount();
        this.expireDate = course.getExpireDate();
        this.status = course.getStatus();
        this.level = course.getLevel();
        this.courseInformationType = course.getCourseInformationType();
        this.coursePrice = course.getCoursePrice();
        this.showPromotion = course.isShowPromotion();
        this.createdDate = course.getCreatedDate();
        this.category = category;
        this.courseAdminFeedback = courseAdminFeedback;
        this.mediaInfo = mediaInfo;

    }

    public CourseResponse(Course course, User user, List<Category> category, CourseAdminFeedback courseAdminFeedback) {
        this.id = course.getId();
        this.userId = course.getUserId();
        this.name = user.getName();
        this.title = course.getTitle();
        this.subtitle = course.getSubtitle();
        this.courseStatus = course.getCourseStatus();
        this.image = course.getImage();
        this.promotionVideo = course.getPromotionVideo();
        this.description = course.getDescription();
        this.language = course.getLanguage();
        this.point = course.getPoint();
        this.discount = course.getDiscount();
        this.expireDate = course.getExpireDate();
        this.status = course.getStatus();
        this.level = course.getLevel();
        this.courseInformationType = course.getCourseInformationType();
        this.coursePrice = course.getCoursePrice();
        this.showPromotion = course.isShowPromotion();
        this.createdDate = course.getCreatedDate();
        this.category = category;
        this.courseAdminFeedback = courseAdminFeedback;
    }

    public CourseResponse(Course course, List<Category> category) {
        this.id = course.getId();
        this.userId = course.getUserId();
        this.title = course.getTitle();
        this.subtitle = course.getSubtitle();
        this.courseStatus = course.getCourseStatus();
        this.image = course.getImage();
        this.promotionVideo = course.getPromotionVideo();
        this.description = course.getDescription();
        this.language = course.getLanguage();
        this.point = course.getPoint();
        this.discount = course.getDiscount();
        this.expireDate = course.getExpireDate();
        this.status = course.getStatus();
        this.level = course.getLevel();
        this.courseInformationType = course.getCourseInformationType();
        this.coursePrice = course.getCoursePrice();
        this.showPromotion = course.isShowPromotion();
        this.createdDate = course.getCreatedDate();
        this.category = category;
    }

    public CourseResponse(Course course, User user) {
        this.id = course.getId();
        this.name = user.getName();
        this.userId = course.getUserId();
        this.title = course.getTitle();
        this.subtitle = course.getSubtitle();
        this.courseStatus = course.getCourseStatus();
        this.image = course.getImage();
        this.promotionVideo = course.getPromotionVideo();
        this.description = course.getDescription();
        this.language = course.getLanguage();
        this.point = course.getPoint();
        this.discount = course.getDiscount();
        this.expireDate = course.getExpireDate();
        this.status = course.getStatus();
        this.level = course.getLevel();
        this.courseInformationType = course.getCourseInformationType();
        this.coursePrice = course.getCoursePrice();
        this.showPromotion = course.isShowPromotion();
        this.createdDate = course.getCreatedDate();
    }

    public CourseResponse(Course course) {
        this.id = course.getId();
        this.userId = course.getUserId();
        this.title = course.getTitle();
        this.subtitle = course.getSubtitle();
        this.courseStatus = course.getCourseStatus();
        this.image = course.getImage();
        this.promotionVideo = course.getPromotionVideo();
        this.description = course.getDescription();
        this.language = course.getLanguage();
        this.point = course.getPoint();
        this.discount = course.getDiscount();
        this.expireDate = course.getExpireDate();
        this.status = course.getStatus();
        this.level = course.getLevel();
        this.courseInformationType = course.getCourseInformationType();
        this.coursePrice = course.getCoursePrice();
        this.showPromotion = course.isShowPromotion();
        this.createdDate = course.getCreatedDate();
    }

    public CourseResponse(Course course, CourseAdminFeedback courseAdminFeedback) {
        this.id = course.getId();
        this.userId = course.getUserId();
        this.title = course.getTitle();
        this.subtitle = course.getSubtitle();
        this.courseStatus = course.getCourseStatus();
        this.image = course.getImage();
        this.promotionVideo = course.getPromotionVideo();
        this.description = course.getDescription();
        this.language = course.getLanguage();
        this.point = course.getPoint();
        this.discount = course.getDiscount();
        this.expireDate = course.getExpireDate();
        this.status = course.getStatus();
        this.level = course.getLevel();
        this.courseInformationType = course.getCourseInformationType();
        this.coursePrice = course.getCoursePrice();
        this.showPromotion = course.isShowPromotion();
        this.createdDate = course.getCreatedDate();
        this.courseAdminFeedback = courseAdminFeedback;
    }

    public CourseResponse(Course course,User user, List<IntendedLearner> intendedLearners, Long totalDuration, double totalUserEnrol) {
        this.id = course.getId();
        this.name = user.getName();
        this.userId = course.getUserId();
        this.title = course.getTitle();
        this.subtitle = course.getSubtitle();
        this.courseStatus = course.getCourseStatus();
        this.image = course.getImage();
        this.promotionVideo = course.getPromotionVideo();
        this.description = course.getDescription();
        this.language = course.getLanguage();
        this.point = course.getPoint();
        this.discount = course.getDiscount();
        this.expireDate = course.getExpireDate();
        this.status = course.getStatus();
        this.level = course.getLevel();
        this.courseInformationType = course.getCourseInformationType();
        this.coursePrice = course.getCoursePrice();
        this.showPromotion = course.isShowPromotion();
        this.createdDate = course.getCreatedDate();
        this.intendedLearners = intendedLearners;
        if (totalDuration != null) {
            this.totalDuration = totalDuration;
        }
        this.totalUserEnrol = totalUserEnrol;
    }

    public CourseResponse(String id, String userId, String name, String title, String point, String courseStatus, String totalUserEnrol) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.title = title;
        this.courseStatus = CourseStatus.valueOf(courseStatus);
        this.point = Double.parseDouble(point);
        this.totalUserEnrol = Double.parseDouble(totalUserEnrol);
    }

    public CourseResponse(Orders orders, List<OrderDetail> orderDetails) {
        this.id = orders.getId();
        this.userId = orders.getUserId();
        this.totalPoint = orders.getTotalPoint();
        this.orderDetails = orderDetails;
        this.createdDate = orders.getCreatedDate();
        this.orderStatus = orders.getOrderStatus();
    }
}
