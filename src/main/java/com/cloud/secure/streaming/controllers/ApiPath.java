package com.cloud.secure.streaming.controllers;

/**
 * @author 689Cloud
 */
public interface ApiPath {
    // Base URL
    String BASE_API_PATH = "/api";
    String ALL = "/all";
    // Authenticate APIs
    String AUTHENTICATE_API = BASE_API_PATH + "/auth";
    // User APIs
    String USER_API = BASE_API_PATH + "/user";
    // Teaching Experiences APIs
    String TEACHING_EXPERIENCE_API = BASE_API_PATH + "/teaching-experience";
    String JOB_EXPERIENCE_API = BASE_API_PATH + "/job-experience";
    // Bank Information APIs
    String BANK_INFORMATION_API = BASE_API_PATH + "/bank-information";
    // Announcement APIs
    String ANNOUNCEMENT_API = BASE_API_PATH + "/announcement";
    String COMMENT_API = BASE_API_PATH + "/comment";
    // Course APIs
    String COURSE_API = BASE_API_PATH + "/course";
    // Category APIs
    String CATEGORY_API = BASE_API_PATH + "/category";
    // Section APIs
    String SECTION_API = BASE_API_PATH + "/section";
    // Intended Learner APIs
    String INTENDED_LEARNER_API = BASE_API_PATH + "/intended-learner";
    // Resource APIs
    String RESOURCE_API = BASE_API_PATH + "/resource";
    // Lecture APIs
    String LECTURE_API = BASE_API_PATH + "/lecture";
    // Upload API
    String UPLOAD_API = BASE_API_PATH + "/upload";
    // certificate API
    String CERTIFICATE_API = BASE_API_PATH + "/certificate";
    // Enrollment API
    String ORDER_API = BASE_API_PATH + "/order";
    // MediaInfo API
    String MEDIA_INFO_API = BASE_API_PATH + "/media-info";
    // PointPackage API
    String POINT_PACKAGE_API = BASE_API_PATH + "/point";
    // Card API
    String CARD_API = BASE_API_PATH + "/card";
    // Rate API
    String RATE_API = BASE_API_PATH + "/rate";

    String EXCHANGE_RATE_API = BASE_API_PATH + "/exchange-rate";
    // Payment API
    String PAYMENT_API = BASE_API_PATH + "/payment";
    // Transaction API
    String TRANSACTION_API = BASE_API_PATH + "/transaction";
    // ReviewCourse API
    String COURSE_REVIEW_API = BASE_API_PATH + "/review-course";
    // CourseReport API
    String COURSE_REPORT_API = BASE_API_PATH + "/course-report";
    // Add Point API
    String ADD_POINT_API = BASE_API_PATH + "/add-point";

    String POINT_HISTORY_API = BASE_API_PATH + "/point-history";
}
