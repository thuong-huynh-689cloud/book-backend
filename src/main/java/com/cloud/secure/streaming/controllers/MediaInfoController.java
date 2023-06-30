package com.cloud.secure.streaming.controllers;

import com.cloud.secure.streaming.amazon.s3.AmazonS3Util;
import com.cloud.secure.streaming.common.enums.APIStatusMessage;
import com.cloud.secure.streaming.common.enums.MediaInfoType;
import com.cloud.secure.streaming.common.enums.UserType;
import com.cloud.secure.streaming.common.exceptions.ApplicationException;
import com.cloud.secure.streaming.common.utilities.ApplicationConfigureValues;
import com.cloud.secure.streaming.common.utilities.RestAPIResponse;
import com.cloud.secure.streaming.common.utilities.RestAPIStatus;
import com.cloud.secure.streaming.common.utilities.Validator;
import com.cloud.secure.streaming.config.security.AuthSession;
import com.cloud.secure.streaming.config.security.AuthUser;
import com.cloud.secure.streaming.config.security.AuthorizeValidator;
import com.cloud.secure.streaming.controllers.helper.MediaInfoHelper;
import com.cloud.secure.streaming.controllers.helper.UploadHelper;
import com.cloud.secure.streaming.controllers.model.response.MediaInfoResponse;
import com.cloud.secure.streaming.entities.MediaInfo;
import com.cloud.secure.streaming.services.CourseService;
import com.cloud.secure.streaming.services.LectureService;
import com.cloud.secure.streaming.services.MediaInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(ApiPath.MEDIA_INFO_API)
@Slf4j
public class MediaInfoController extends AbstractBaseController{


    final ApplicationConfigureValues appConfig;
    final MediaInfoHelper mediaInfoHelper;
    final MediaInfoService mediaInfoService;



    public MediaInfoController(ApplicationConfigureValues appConfig, MediaInfoHelper mediaInfoHelper,
                               MediaInfoService mediaInfoService) {

        this.appConfig = appConfig;
        this.mediaInfoHelper = mediaInfoHelper;
        this.mediaInfoService = mediaInfoService;


    }



    /**
     * Get Detail MediaInfo API
     *
     * @param authUser
     * @param id
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR, UserType.LEARNER})
    @GetMapping(path = "/{id}")
    @Operation(summary = "Get Detail MediaInfo")
    public ResponseEntity<RestAPIResponse> getDetailMediaInfo(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @PathVariable(name = "id") String id
    ) {
        MediaInfo mediaInfo = mediaInfoService.getMediaByActiveOrPending(id);
        Validator.notNull(mediaInfo, RestAPIStatus.NOT_FOUND, APIStatusMessage.MEDIA_INFO_NOT_FOUND);
        if (authUser.getType().equals(UserType.INSTRUCTOR)) {

            if (!authUser.getId().equals(mediaInfo.getUserId())) {
                throw new ApplicationException(RestAPIStatus.FORBIDDEN,APIStatusMessage.CAN_ONLY_VIEW_YOUR_OWN_INFORMATION);
            }
        }

        // check LEARNER can only be obtained once the course has been purchased
        // todo
        String url;

        if (mediaInfo.isEncrypt()) {
            url = appConfig.streamingApiPrefix + appConfig.encrypted + mediaInfo.getId() + "/" + mediaInfo.getId() + "." + mediaInfo.getExtension();
        } else {
            url = appConfig.streamingApiPrefix + appConfig.nonEncrypted + mediaInfo.getId() + "/" + mediaInfo.getId() + "." + mediaInfo.getExtension();
        }

        return responseUtil.successResponse(new MediaInfoResponse(mediaInfo, url));
    }

    /**
     * Get MediaInfo ByLectureIdOrCourseId API
     *
     * @param id
     * @param mediaInfoType
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR, UserType.LEARNER})
    @GetMapping(path = "/media-info-type/{id}")
    @Operation(summary = "Get MediaInfo By LectureId Or CourseId")
    public ResponseEntity<RestAPIResponse> getMediaInfoByLectureIdOrCourseId(
            @PathVariable(name = "id") String id,
            @RequestParam(name = "media_info_type") MediaInfoType mediaInfoType
    ) {
        MediaInfo mediaInfo = mediaInfoService.getMediaInfoByCourseIdOrLectureIdAndMediaInfoType(id, mediaInfoType);
        Validator.notNull(mediaInfo, RestAPIStatus.NOT_FOUND, APIStatusMessage.COURSE_NOT_FOUND);

        return responseUtil.successResponse(mediaInfo);
    }

    /**
     * Get ALl MediaInfo API
     *
     * @param authUser
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR, UserType.LEARNER})
    @GetMapping(path = "all")
    @Operation(summary = "Get All MediaInfo")
    public ResponseEntity<RestAPIResponse> getAllMediaInfo(
            @Parameter(hidden = true) @AuthSession AuthUser authUser
    ) {
        // check LEARNER can only be obtained once the course has been purchased
        // todo
        List<MediaInfoResponse> mediaInfoResponses = new ArrayList<>();
        // get all mediaInfo
        List<MediaInfo> mediaInfos = mediaInfoService.getAllByUserIdOrderByCreatedDateDesc(authUser.getId());

        if (mediaInfos != null) {
            mediaInfos.forEach(mediaInfo -> {

                String url;
                if (mediaInfo.isEncrypt()) {
                    url = appConfig.streamingApiPrefix + mediaInfo.getId() + "." + mediaInfo.getExtension();
                } else {
                    url = appConfig.streamingApiPrefix + appConfig.nonEncrypted + mediaInfo.getId() + "." + mediaInfo.getExtension();
                }
                MediaInfoResponse mediaInfoResponse = new MediaInfoResponse(mediaInfo, url);
                mediaInfoResponses.add(mediaInfoResponse);
            });
        }
        return responseUtil.successResponse(mediaInfoResponses);
    }

    /**
     * Delete MediaInfos API
     *
     * @param authUser
     * @param ids
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR})
    @DeleteMapping
    @Operation(summary = "Delete MediaInfos")
    public ResponseEntity<RestAPIResponse> deleteMediaInfo(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @RequestParam(name = "ids") List<String> ids
    ) {
        MediaInfo mediaInfo = mediaInfoService.getByIdIn(ids);
        if (authUser.getType().equals(UserType.INSTRUCTOR)) {
            if (mediaInfo != null) {
                if (!authUser.getId().equals(mediaInfo.getUserId())) {
                    throw new ApplicationException(RestAPIStatus.FORBIDDEN,APIStatusMessage.CAN_ONLY_DELETE_YOUR_OWN_INFORMATION);
                }
            }
        }
        // delete user
        mediaInfoService.deleteMediaInfoByIdIn(ids);
        return responseUtil.successResponse("Ok");
    }
}
