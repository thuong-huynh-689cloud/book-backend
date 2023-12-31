package com.cloud.secure.streaming.common.enums;

public enum APIStatusMessage {
    INVALID_EMAIL_FORMAT,
    INVALID_URL,
    INVALID_BANK_ACCOUNT_NUMBER,
    EMAIL_EXISTED,
    MEDIAINFO_EXISTED,
    PASSWORD_DOES_NOT_MATCH,
    PHONE_DIAL_CODE_MUST_NOT_BE_NULL,
    CAN_ONLY_VIEW_YOUR_OWN_INFORMATION,
    CAN_ONLY_UPDATE_YOUR_OWN_INFORMATION,
    CAN_ONLY_DELETE_YOUR_OWN_INFORMATION,
    CAN_ONLY_CREATE_YOUR_OWN_INFORMATION,
    USER_NOT_FOUND,
    USER_ID_IS_EMPTY,
    COURSE_NOT_FOUND,
    LECTURE_NOT_FOUND,
    MEDIA_INFO_NOT_FOUND,
    TEACHING_EXPERIENCE_NOT_FOUND,
    SECTION_NOT_FOUND,
    RESOURCE_NOT_FOUND,
    RATE_NOT_FOUND,
    POINT_NOT_FOUND,
    ENROLMENT_NOT_FOUND,
    JOB_EXPERIENCE_NOT_FOUND,
    INTENDED_LEARNER_NOT_FOUND,
    EXCHANGE_RATE_NOT_FOUND,
    CATEGORY_NOT_FOUND,
    ANNOUNCEMENT_NOT_FOUND,
    COMMENT_NOT_FOUND,
    CARD_NOT_FOUND,
    BANK_INFORMATION_NOT_FOUND,
    INVALID_AUTHENTICATE_CREDENTIAL,
    RESET_CODE_NOT_FOUND,
    CODE_NOT_FOUND,
    INVALID_CONFIRM_CODE_OR_CONFIRM_CODE_IS_EXPIRED,
    INTERNAL_SERVER_ERROR,
    UNAUTHORIZED,
    CANNOT_CHANGE_THIS_USERS_PASSWORD,
    COURSE_ID_AND_LECTURE_ID_CAN_BE_NULL_AT_THE_SAME_TIME,
    COURSE_ID_AND_LECTURE_ID_CAN_BE_PRESENT_AT_THE_SAME_TIME,
    INVALID_PAGE_NUMBER_OR_PAGE_SIZE,
    THIS_RESOURCE_TYPE_DOES_NOT_HAVE_LINK,
    THIS_RESOURCE_TYPE_MUST_NOT_FILE_NAME,
    THIS_RESOURCE_TYPE_MUST_HAVE_MUST_HAVE_TITLE,
    EXTERNAL_RESOURCE_MUST_HAVE_TITLE,
    USER_ONLY_CREATE_RESOURCE_FOR_THEIR_COURSE,
    INSTRUCTOR_NOT_FOUND,
    CANNOT_IMPLEMENTS_FOR_OTHER_USERS,
    MUST_HAVE_COURSE_ID,
    CANNOT_UPDATE_STATUS_DRAFT_OR_WAITING_FOR_APPROVAL_FOR_THIS_COURSE,
    CANNOT_UPDATE_STATUS_PUBLISH_OR_REJECT_FOR_THIS_COURSE,
    INVALID_DOB_FORMAT_MM_DD_YYYY,
    THE_LINK_IS_EXPIRED,
    CURRENT_PASSWORD_IS_INCORRECT,
    NEW_PASSWORD_MUST_BE_DIFFERENT_FROM_CURRENT_PASSWORD,
    CONFIRM_PASSWORD_DOES_NOT_FOUND,
    INVALID_SOCIAL_TYPE,
    CERTIFICATE_NOT_FOUND,
    INVALID_FILE_SIZE,
    INVALID_EXTENSION,
    CONFIRM_PASSWORD_DOES_NOT_MATCH,
    NEW_PASSWORD_CANNOT_THE_SAME_WITH_THE_OLD_ONE,
    COURSE_ODER_DETAILS_EXISTED,
    INVALID_UPLOAD_AVATAR,
    INVALID_UPLOAD_COURSE_IMAGE,
    INVALID_UPLOAD_RESOURCE,
    INVALID_UPLOAD_LECTURE,
    NOT_ENOUGH_POINT,
    CANNOT_UPDATE_WHEN_STATUS_IS_PUBLISHED,
    ORDER_NOT_FOUND,
    TRANSACTION_NOT_FOUND,
    PURCHASED_COURSE,
    CANNOT_ADD_CARD,
    CARD_HAS_NOT_BEEN_ADDED,
    POINT_HISTORY_NOT_FOUND,
    STRIPE_CANT_CHARGE,
    ORDER_EXISTED,
    CANNOT_CREATE_WHEN_STATUS_IS_PUBLISHED,
    UPDATE_ONLY_WHEN_STATUS_IS_WAITING_FOR_APPROVAL
}
