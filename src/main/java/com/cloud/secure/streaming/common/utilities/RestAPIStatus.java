package com.cloud.secure.streaming.common.utilities;

/**
 * @author 689Cloud
 */
public enum RestAPIStatus {
    OK(200, "OK"),
    NO_RESULT(201, "No more result."),
    FAIL(202, "Fail"),
    BAD_REQUEST(400, "Bad request"),
    UNAUTHORIZED(401, "Unauthorized or Access Token is expired"),
    INVALID_AUTHENTICATE_CREDENTIAL(402, "Invalid authenticated credential"),
    FORBIDDEN(403, "Forbidden! Access denied"),
    NOT_FOUND(404, "Not Found"),
    EXISTED(405, "Already existed"),
    BAD_PARAMS(406, "There is some invalid data"),
    EXPIRED(407, "Expired"),
    INTERNAL_SERVER_ERROR(500, "Internal server error"),
    INVALID_CODE(601, "Invalid code"),
    CANNOT_ENCRYPT_RANDOM_PASSWORD(602, "Cannot encrypt random password"),

    Null(200, "null"),

    ERR_INVALID_FILE(700, "Invalid file"),
    ERR_TOO_BIG_AVATAR_FILE(701, "File is too big, Max 2MB per file"),
    ERR_TOO_BIG_COURSE_IMAGE_FILE(701,"File is too big, Max 10MB per file"),
    ERR_TOO_BIG_RESOURCE_FILE(701,"File is too big, Max 1GB per file"),
    ERR_TOO_BIG_CERTIFICATE_FILE(701, "File is too big, Max 12MB per file"),
    ERR_TOO_BIG_LECTURE_FILE(701, "File is too big, Max 4GB per file"),


    ERR_STRIPE (901, "The error occurred because STRIPE returns"),
    ERR_CREATE_CARD(902, "Cannot create card"),
    ERR_CARD_CVC(903, "Invalid CVC number"),

    ;

    private final int code;
    private final String description;

    private RestAPIStatus(int s, String v) {
        code = s;
        description = v;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static RestAPIStatus getEnum(int code) {
        for (RestAPIStatus v : values())
            if (v.getCode() == code) return v;
        throw new IllegalArgumentException();
    }
}
