package com.cloud.secure.streaming.common.utilities;

/**
 * @author 689Cloud
 */
public interface Constant {
    String HEADER_TOKEN = "Auth-Token";

    //  Date
    String API_FORMAT_DATE_TIME = "MM/dd/yyyy hh:mm:ss";
    String API_FORMAT_TIME = "MM/dd/yyyy HH:mm";
    String API_FORMAT_DATE = "MM/dd/yyyy";
    String FORMAT_TIME = "HH:mm";
    long ONE_DAY_MILLISECOND = 24 * 60 * 60 * 1000;

    // Regex Pattern
    String XSS_PATTERN = "^((?!<|>)[\\s\\S])*?$";
    String CURLY_BRACES_PATTERN = "^((?!\\{|\\})[\\s\\S])*?$";
    String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    String BANK_ACCOUNT_NUMBER_PATTERN = "^[0-9]{6,20}$";
    String URL_PATTERN = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
    String WEBSITE_PATTERN = "^(http(s)?\\:\\/\\/)?([\\w-]+\\.)+[\\w-]+([\\w-./?%&=]*)?$";
    String TWITTER_PATTERN = "^(http(s)?\\:\\/\\/)?(www\\.)?twitter\\.com\\/[A-Za-z0-9_]{1,15}(\\/\\w*)?$";
    String FACEBOOK_PATTERN = "^(http(s)?\\:\\/\\/)?(www\\.)?facebook\\.com\\/[A-Za-z0-9\\.\\-]{1,50}(\\/\\w*)?$";
    String LINKEDIN_PATTERN = "^(http(s)?\\:\\/\\/)?([\\w-]+\\.)+[\\w-]+([\\w-./?%&=]*)?$";
    String YOUTUBE_PATTERN = "^(https?\\:\\/\\/)?(www\\.youtube\\.com|youtu\\.?be)\\/.+$";

    // Other
    int SALT_LENGTH = 6;
    // extension
    String[] VIDEO_EXTENSION = {"mp4", "mov", "wmv", "flv", "avi", "avchd", "webm", "mkv"};
    String[] AVATAR_EXTENSION = {"png", "pjp", "jpg", "jpeg", "jfif", "pjpeg", "ico"};
    String[] COURSE_IMAGE_EXTENSION = {"png", "jpg", "jpeg", "gif"};
    String[] CERTIFICATE_EXTENSION = {"docx", "pdf","doc"};
    String[] LECTURE_EXTENSION = {"mp4", "mov", "wmv", "flv", "avi", "avchd", "webm", "mkv","docx","docx", "pdf"};

}

