package com.cloud.secure.streaming.controllers;

import com.cloud.secure.streaming.common.enums.APIStatusMessage;
import com.cloud.secure.streaming.common.enums.UserType;
import com.cloud.secure.streaming.common.utilities.RestAPIResponse;
import com.cloud.secure.streaming.common.utilities.RestAPIStatus;
import com.cloud.secure.streaming.common.utilities.Validator;
import com.cloud.secure.streaming.config.security.AuthSession;
import com.cloud.secure.streaming.config.security.AuthUser;
import com.cloud.secure.streaming.config.security.AuthorizeValidator;
import com.cloud.secure.streaming.controllers.helper.JobExperienceHelper;
import com.cloud.secure.streaming.controllers.helper.TeachingExperienceHelper;
import com.cloud.secure.streaming.controllers.model.request.CreateTeachingExperienceRequest;
import com.cloud.secure.streaming.controllers.model.request.UpdateTeachingExperienceRequest;
import com.cloud.secure.streaming.entities.JobExperience;
import com.cloud.secure.streaming.entities.TeachingExperience;
import com.cloud.secure.streaming.services.JobExperienceService;
import com.cloud.secure.streaming.services.TeachingExperienceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping(ApiPath.TEACHING_EXPERIENCE_API)
@Slf4j
public class TeachingExperienceController extends AbstractBaseController {
    final TeachingExperienceService teachingExperiencesService;
    final TeachingExperienceHelper teachingExperienceHelper;
    final JobExperienceService jobExperienceService;
    final JobExperienceHelper jobExperienceHelper;

    public TeachingExperienceController(TeachingExperienceService teachingExperiencesService,
                                        TeachingExperienceHelper teachingExperienceHelper,
                                        JobExperienceService jobExperienceService, JobExperienceHelper jobExperienceHelper
    ) {
        this.teachingExperiencesService = teachingExperiencesService;
        this.teachingExperienceHelper = teachingExperienceHelper;
        this.jobExperienceService = jobExperienceService;
        this.jobExperienceHelper = jobExperienceHelper;
    }

    /**
     * Create Teaching Experiences API
     *
     * @param createTeachingExperienceRequest
     * @return
     */
    @AuthorizeValidator({UserType.INSTRUCTOR, UserType.SYSTEM_ADMIN})
    @PostMapping(value = "/{job_id}")
    @Operation(summary = "Create Teaching Experiences")
    public ResponseEntity<RestAPIResponse> createTeachingExperiences(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @RequestBody @Valid List<CreateTeachingExperienceRequest> createTeachingExperienceRequest,
            @PathVariable("job_id") String jobId
    ) {
        List<TeachingExperience> teachingExperiences = new ArrayList<>();
        // get jobExperience
        JobExperience jobExperience = jobExperienceService.getById(jobId);
        Validator.notNull(jobExperience, RestAPIStatus.NOT_FOUND, APIStatusMessage.TEACHING_EXPERIENCE_NOT_FOUND);

        if (authUser.getType().equals(UserType.INSTRUCTOR)) {
            Validator.mustEquals(authUser.getId(), jobExperience.getUserId(), RestAPIStatus.FORBIDDEN, APIStatusMessage.CAN_ONLY_CREATE_YOUR_OWN_INFORMATION);
        }
        // Check createTeachingExperienceRequest
        if (createTeachingExperienceRequest != null && !createTeachingExperienceRequest.isEmpty()) {
            for (CreateTeachingExperienceRequest teachingExperienceRequest : createTeachingExperienceRequest) {
                // Create teachingExperience
                TeachingExperience teachingExperience = teachingExperienceHelper.createTeachingExperience(jobId, teachingExperienceRequest);
                teachingExperiences.add(teachingExperience);
            }
        }

        if (!teachingExperiences.isEmpty()) {
            teachingExperiencesService.saveAll(teachingExperiences);
        }
        return responseUtil.successResponse(teachingExperiences);
    }

    /**
     * Get detail Teaching Experience API
     *
     * @param id
     * @return
     */
    @AuthorizeValidator({UserType.INSTRUCTOR, UserType.SYSTEM_ADMIN})
    @GetMapping(path = "/{id}")
    @Operation(summary = "Det Detail Teaching Experience")
    public ResponseEntity<RestAPIResponse> getDetailTeachingExperience(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @PathVariable(name = "id") String id // Teaching Experience ID
    ) {
        // Get TeachingExperience
        TeachingExperience teachingExperience = teachingExperiencesService.getById(id);
        Validator.notNull(teachingExperience, RestAPIStatus.NOT_FOUND, APIStatusMessage.TEACHING_EXPERIENCE_NOT_FOUND);
        JobExperience jobExperience = jobExperienceService.getById(teachingExperience.getJobExperienceId());
        if (authUser.getType().equals(UserType.INSTRUCTOR)) {
            if (jobExperience != null) {
                Validator.mustEquals(authUser.getId(), jobExperience.getUserId(), RestAPIStatus.FORBIDDEN, APIStatusMessage.CAN_ONLY_CREATE_YOUR_OWN_INFORMATION);
            }
        }

        return responseUtil.successResponse(teachingExperience);
    }

    /**
     * Get List Teaching Experiences API
     *
     * @param jobId
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR, UserType.LEARNER})
    @GetMapping("/job/{job_id}")
    @Operation(summary = "Get List Teaching Experiences")
    public ResponseEntity<RestAPIResponse> getListTeachingExperiences(
            @PathVariable("job_id") String jobId
    ) {
        // Get list TeachingExperience
        List<TeachingExperience> teachingExperiences = teachingExperiencesService.getAllByJobExperienceIdOrderByCreatedDateDesc(jobId);

        return responseUtil.successResponse(teachingExperiences);
    }

    /**
     * Update Teaching Experience API
     *
     * @param authUser
     * @param id
     * @param updateTeachingExperienceRequest
     * @return
     */
    @AuthorizeValidator({UserType.INSTRUCTOR, UserType.SYSTEM_ADMIN})
    @PutMapping(path = "/{id}")
    @Operation(summary = "Update Teaching Experience")
    public ResponseEntity<RestAPIResponse> updateTeachingExperience(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @PathVariable(name = "id") String id,
            @RequestBody @Valid UpdateTeachingExperienceRequest updateTeachingExperienceRequest
    ) {
        // Get TeachingExperience
        TeachingExperience teachingExperience = teachingExperiencesService.getById(id);
        Validator.notNull(teachingExperience, RestAPIStatus.NOT_FOUND, APIStatusMessage.TEACHING_EXPERIENCE_NOT_FOUND);
        // Get jobExperience
        JobExperience jobExperience = jobExperienceService.getById(teachingExperience.getJobExperienceId());
        if (authUser.getType().equals(UserType.INSTRUCTOR)) {
            if (jobExperience != null) {
                Validator.mustEquals(authUser.getId(), jobExperience.getUserId(), RestAPIStatus.FORBIDDEN,APIStatusMessage.CAN_ONLY_CREATE_YOUR_OWN_INFORMATION);
            }
        }
        // update teachingExperience
        teachingExperienceHelper.updateTeachingExperience(teachingExperience, updateTeachingExperienceRequest);
        teachingExperiencesService.save(teachingExperience);

        return responseUtil.successResponse(teachingExperience);
    }

    /**
     * Delete Teaching Experience API
     *
     * @param ids
     * @return
     */
    @AuthorizeValidator({UserType.INSTRUCTOR, UserType.SYSTEM_ADMIN})
    @DeleteMapping
    @Operation(summary = "Delete Teaching Experience")
    public ResponseEntity<RestAPIResponse> deleteTeachingExperience(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @RequestParam(name = "ids") List<String> ids
    ) {
        if (UserType.SYSTEM_ADMIN.equals(authUser.getType())) {
            // delete jobExperience
            teachingExperiencesService.deleteTeachingExperienceByIdIn(ids);
        } else {
            teachingExperiencesService.deleteTeachingExperienceByIdInAndUserId(ids, authUser.getId());
        }

        return responseUtil.successResponse("OK");
    }
}
