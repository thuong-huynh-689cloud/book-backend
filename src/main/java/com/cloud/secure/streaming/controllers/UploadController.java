package com.cloud.secure.streaming.controllers;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.cloud.secure.streaming.amazon.s3.AmazonS3Util;
import com.cloud.secure.streaming.common.enums.*;
import com.cloud.secure.streaming.common.exceptions.ApplicationException;
import com.cloud.secure.streaming.common.utilities.*;
import com.cloud.secure.streaming.config.security.AuthSession;
import com.cloud.secure.streaming.config.security.AuthUser;
import com.cloud.secure.streaming.config.security.AuthorizeValidator;
import com.cloud.secure.streaming.controllers.helper.MediaInfoHelper;
import com.cloud.secure.streaming.controllers.helper.UploadHelper;
import com.cloud.secure.streaming.controllers.model.request.*;
import com.cloud.secure.streaming.entities.Course;
import com.cloud.secure.streaming.entities.Lecture;
import com.cloud.secure.streaming.entities.MediaInfo;
import com.cloud.secure.streaming.scheduler.queue.VideoEncryptQueueManager;
import com.cloud.secure.streaming.scheduler.queue.message.VideoEncryptQueueMessage;
import com.cloud.secure.streaming.services.CourseService;
import com.cloud.secure.streaming.services.LectureService;
import com.cloud.secure.streaming.services.MediaInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(ApiPath.UPLOAD_API)
@Slf4j
public class UploadController extends AbstractBaseController {

    final UploadHelper uploadHelper;
    final ApplicationConfigureValues appConfig;
    final MediaInfoHelper mediaInfoHelper;
    final MediaInfoService mediaInfoService;
    final CourseService courseService;
    final LectureService lectureService;
    final AmazonS3Util amazonS3Util;

    public UploadController(UploadHelper uploadHelper, ApplicationConfigureValues appConfig, MediaInfoHelper mediaInfoHelper,
                            MediaInfoService mediaInfoService, CourseService courseService, LectureService lectureService,
                            AmazonS3Util amazonS3Util) {
        this.uploadHelper = uploadHelper;
        this.appConfig = appConfig;
        this.mediaInfoHelper = mediaInfoHelper;
        this.mediaInfoService = mediaInfoService;
        this.courseService = courseService;
        this.lectureService = lectureService;
        this.amazonS3Util = amazonS3Util;
    }

    /**
     * Upload Video API
     *
     * @param request
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR})
    @PostMapping(path = "/promotion-video")
    @Operation(summary = "Upload Promotion Video")
    public ResponseEntity<RestAPIResponse> uploadPromotionVideo(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @Valid @RequestBody UploadPromotionVideoRequest uploadPromotionVideoRequest,
            HttpServletRequest request
    ) {
        // get mediaInfo and check duplicate mediaInfo
        MediaInfo mediaInfos = mediaInfoService.getByIdAndStatus(uploadPromotionVideoRequest.getName(), AppStatus.ACTIVE);
        Validator.mustNull(mediaInfos, RestAPIStatus.EXISTED, APIStatusMessage.MEDIAINFO_EXISTED);

        // check extension video
        Validator.mustIn(uploadPromotionVideoRequest.getExtension(),Constant.VIDEO_EXTENSION,RestAPIStatus.BAD_REQUEST,APIStatusMessage.INVALID_EXTENSION);

        // check CourseId not null and not isEmpty
        if (uploadPromotionVideoRequest.getCourseId() != null && !uploadPromotionVideoRequest.getCourseId().isEmpty()) {
            Course course = courseService.getById(uploadPromotionVideoRequest.getCourseId());
            Validator.notNull(course, RestAPIStatus.NOT_FOUND, APIStatusMessage.COURSE_NOT_FOUND);
        }
        // check id LectureId not null and not isEmpty
        if (uploadPromotionVideoRequest.getLectureId() != null && !uploadPromotionVideoRequest.getLectureId().isEmpty()) {
            Lecture lecture = lectureService.getById(uploadPromotionVideoRequest.getLectureId());
            Validator.notNull(lecture, RestAPIStatus.NOT_FOUND, APIStatusMessage.LECTURE_NOT_FOUND);
        }
        // check 2 ids cannot be null together
        if ((uploadPromotionVideoRequest.getCourseId() == null || uploadPromotionVideoRequest.getCourseId().isEmpty()) &&
                ((uploadPromotionVideoRequest.getLectureId() == null) || (uploadPromotionVideoRequest.getLectureId().isEmpty()))) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, APIStatusMessage.COURSE_ID_AND_LECTURE_ID_CAN_BE_NULL_AT_THE_SAME_TIME);
        }
        // check can't have 2 ids at the same time
        if ((uploadPromotionVideoRequest.getCourseId() != null && !uploadPromotionVideoRequest.getCourseId().isEmpty()) &&
                (uploadPromotionVideoRequest.getLectureId() != null && !uploadPromotionVideoRequest.getLectureId().isEmpty())) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, APIStatusMessage.COURSE_ID_AND_LECTURE_ID_CAN_BE_PRESENT_AT_THE_SAME_TIME);
        }

        MediaInfo mediaInfo = null;

        // get oldMediaInfo if already have CourseId or LectureId
        MediaInfo oldMediaInfo = uploadPromotionVideoRequest.getCourseId() != null && !uploadPromotionVideoRequest.getCourseId().isEmpty()
                ? mediaInfoService.getMediaInfoByCourseIdAndMediaInfoTypeAndStatus(uploadPromotionVideoRequest.getCourseId(), MediaInfoType.COURSE, AppStatus.ACTIVE) :
                mediaInfoService.getMediaInfoByLectureIdAndMediaInfoTypeAndStatus(uploadPromotionVideoRequest.getLectureId(), MediaInfoType.LECTURE, AppStatus.ACTIVE);

        if (uploadPromotionVideoRequest.getIsEncrypt() != null && uploadPromotionVideoRequest.getIsEncrypt()) {

            // create mediaInfo
            mediaInfo = mediaInfoHelper.createMediaInfo(uploadPromotionVideoRequest.getName(), authUser.getId(),
                    uploadPromotionVideoRequest.getExtension(), uploadPromotionVideoRequest.getNameOriginal(),
                    uploadPromotionVideoRequest.getLectureId(), uploadPromotionVideoRequest.getCourseId(), AppStatus.PENDING,
                    uploadPromotionVideoRequest.getVideoType(),
                    true);

            if (oldMediaInfo != null) {

                if (oldMediaInfo.getCourseId() != null && uploadPromotionVideoRequest.getCourseId() != null ||
                        oldMediaInfo.getLectureId() != null && uploadPromotionVideoRequest.getLectureId() != null) {
                    String keyName = appConfig.uploadPath + "/" + appConfig.encrypted + appConfig.pathVideoInput + "/" +
                            oldMediaInfo.getId() + "." + oldMediaInfo.getExtension();
                    // delete file s3
                    amazonS3Util.deleteFile(keyName);
                    // delete folder s3
                    String keyFolder = appConfig.uploadPath + "/" + oldMediaInfo.getId();
                    amazonS3Util.deleteFolder(keyFolder);
                    //delete old mediainfo
                    mediaInfoService.delete(oldMediaInfo);
                }
            }
            // save mediainfo
            mediaInfoService.save(mediaInfo);
            addToStreamingQueue(uploadPromotionVideoRequest.getName(), request.getServletContext().getRealPath("/"),
                    uploadPromotionVideoRequest.getExtension());

        } else {
            // create mediaInfo
            mediaInfo = mediaInfoHelper.createMediaInfo(uploadPromotionVideoRequest.getName(), authUser.getId(),
                    uploadPromotionVideoRequest.getExtension(), uploadPromotionVideoRequest.getNameOriginal(),
                    uploadPromotionVideoRequest.getLectureId(), uploadPromotionVideoRequest.getCourseId(), AppStatus.ACTIVE,
                    uploadPromotionVideoRequest.getVideoType(), false);

            if (oldMediaInfo != null) {

                if (oldMediaInfo.getCourseId() != null && uploadPromotionVideoRequest.getCourseId() != null ||
                        oldMediaInfo.getLectureId() != null && uploadPromotionVideoRequest.getLectureId() != null) {

                    String keyName = appConfig.uploadPath + "/" + appConfig.nonEncrypted + appConfig.pathVideoInput + "/" +
                            oldMediaInfo.getId() + "." + oldMediaInfo.getExtension();
                    // delete file s3
                    amazonS3Util.deleteFile(keyName);
                    // delete folder s3
                    String keyFolder = appConfig.uploadPath + "/" + oldMediaInfo.getId();
                    amazonS3Util.deleteFolder(keyFolder);
                    //delete old mediainfo
                    mediaInfoService.delete(oldMediaInfo);
                }
            }
            // save mediainfo
            mediaInfoService.save(mediaInfo);
        }

        return responseUtil.successResponse(mediaInfo);
    }

    private void addToStreamingQueue(String name, String path, String extension) {

        VideoEncryptQueueMessage message = new VideoEncryptQueueMessage();
        message.setFileId(name);
        message.setFileName(name + "." + extension);
        message.setMessageId(UniqueID.getUUID());
        message.setFilePath(path);
        VideoEncryptQueueManager.getInstance().addVideoEncryptMessage(message);
    }

    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR, UserType.LEARNER})
    @PostMapping()
    @Operation(summary = "Upload")
    public ResponseEntity<RestAPIResponse> upload(
            @RequestPart("file") MultipartFile multipartFile,
            HttpServletRequest request,
            @RequestParam("canned_access_control_list") CannedAccessControlList cannedAccessControlList,
            @RequestParam("upload_file_type") UploadFileType uploadFileType
    ) {
        int fileSize = (int) (multipartFile.getSize()/1024/1024);
        String extensionType = FilenameUtils.getExtension(multipartFile.getOriginalFilename()).toLowerCase();
        switch (uploadFileType) {
            case UPLOAD_CERTIFICATE:
                // check file size < 12MB
                Validator.mustLessThan(12, RestAPIStatus.BAD_PARAMS, APIStatusMessage.INVALID_FILE_SIZE, fileSize);
                Validator.mustIn(extensionType, Constant.CERTIFICATE_EXTENSION, RestAPIStatus.BAD_PARAMS, APIStatusMessage.INVALID_UPLOAD_LECTURE);
                break;
            case UPLOAD_LECTURE:
                // check file size < 4GB
                Validator.mustLessThan(4*1024, RestAPIStatus.BAD_PARAMS, APIStatusMessage.INVALID_FILE_SIZE, fileSize);
                Validator.mustIn(extensionType, Constant.LECTURE_EXTENSION, RestAPIStatus.BAD_PARAMS, APIStatusMessage.INVALID_UPLOAD_LECTURE);

                break;
            case UPLOAD_RESOURCE:
                // check file size < 1GB
                Validator.mustLessThan(1*1024, RestAPIStatus.BAD_PARAMS, APIStatusMessage.INVALID_FILE_SIZE, fileSize);
                break;
            case UPLOAD_AVATAR:
                //  check File Size < 2MB
                Validator.mustLessThan(2, RestAPIStatus.BAD_PARAMS, APIStatusMessage.INVALID_FILE_SIZE, fileSize);
                Validator.mustIn(extensionType, Constant.AVATAR_EXTENSION, RestAPIStatus.BAD_PARAMS, APIStatusMessage.INVALID_UPLOAD_AVATAR);
                break;
            case UPLOAD_COURSE_IMAGE:
                // check File Size < 10MB
                Validator.mustLessThan(10, RestAPIStatus.BAD_PARAMS, APIStatusMessage.INVALID_FILE_SIZE, fileSize);
                Validator.mustIn(extensionType, Constant.COURSE_IMAGE_EXTENSION, RestAPIStatus.BAD_PARAMS, APIStatusMessage.INVALID_UPLOAD_COURSE_IMAGE);
                break;
        }
        // upload file
        String fileName = uploadHelper.uploadFile(multipartFile, request, cannedAccessControlList, applicationConfigureValues, amazonS3Util);

        return responseUtil.successResponse(fileName);
    }
}
