package com.cloud.secure.streaming.controllers;

import com.cloud.secure.streaming.common.enums.APIStatusMessage;
import com.cloud.secure.streaming.common.enums.UserType;
import com.cloud.secure.streaming.common.exceptions.ApplicationException;
import com.cloud.secure.streaming.common.utilities.RestAPIResponse;
import com.cloud.secure.streaming.common.utilities.RestAPIStatus;
import com.cloud.secure.streaming.common.utilities.Validator;
import com.cloud.secure.streaming.config.security.AuthSession;
import com.cloud.secure.streaming.config.security.AuthUser;
import com.cloud.secure.streaming.config.security.AuthorizeValidator;
import com.cloud.secure.streaming.controllers.helper.JobExperienceHelper;
import com.cloud.secure.streaming.controllers.helper.TeachingExperienceHelper;
import com.cloud.secure.streaming.controllers.model.request.CreateJobExpRequest;
import com.cloud.secure.streaming.controllers.model.request.CreateJobExperiencesRequest;
import com.cloud.secure.streaming.controllers.model.request.CreateTeachingExperienceRequest;
import com.cloud.secure.streaming.controllers.model.request.UpdateJobExperienceRequest;
import com.cloud.secure.streaming.controllers.model.response.JobExperienceAndTeachingExperienceResponse;
import com.cloud.secure.streaming.entities.JobExperience;
import com.cloud.secure.streaming.entities.TeachingExperience;
import com.cloud.secure.streaming.entities.User;
import com.cloud.secure.streaming.services.JobExperienceService;
import com.cloud.secure.streaming.services.TeachingExperienceService;
import com.cloud.secure.streaming.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(ApiPath.JOB_EXPERIENCE_API)
@Slf4j
public class JobExperiencesController extends AbstractBaseController {

    final JobExperienceHelper jobExperienceHelper;
    final JobExperienceService jobExperienceService;
    final TeachingExperienceService teachingExperienceService;
    final TeachingExperienceHelper teachingExperienceHelper;
    final UserService userService;

    public JobExperiencesController(JobExperienceHelper jobExperienceHelper, JobExperienceService jobExperienceService, TeachingExperienceService teachingExperienceService, TeachingExperienceHelper teachingExperienceHelper, UserService userService) {
        this.jobExperienceHelper = jobExperienceHelper;
        this.jobExperienceService = jobExperienceService;
        this.teachingExperienceService = teachingExperienceService;
        this.teachingExperienceHelper = teachingExperienceHelper;
        this.userService = userService;
    }

    /**
     * Create Job Experiences API
     *
     * @param authUser
     * @param jobExpRequest
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR})
    @PostMapping
    @Operation(summary = "Create Job Experiences")
    public ResponseEntity<RestAPIResponse> createJobExperiences(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @RequestBody @Valid CreateJobExpRequest jobExpRequest
    ) {

        String userId = null;

        if (authUser.getType().equals(UserType.SYSTEM_ADMIN)) {
            User user = null;
            // validate user exist
            if (jobExpRequest.getUserId() != null) {
                user = userService.getByIdAndType(jobExpRequest.getUserId(), UserType.INSTRUCTOR);
            }
            if (jobExpRequest.getUserId() == null || user == null) {
                throw new ApplicationException(RestAPIStatus.NOT_FOUND, APIStatusMessage.INSTRUCTOR_NOT_FOUND);
            }
            userId = user.getId();
        }
        if (authUser.getType().equals(UserType.INSTRUCTOR)) {
            userId = authUser.getId();
        }

        List<JobExperience> jobExperiences = new ArrayList<>();
        // add teaching
        List<TeachingExperience> teachingExperiences = new ArrayList<>();
        List<JobExperienceAndTeachingExperienceResponse> jobExperienceAndTeachingExperienceResponses = new ArrayList<>();
        // Check JobExperiences
        for (CreateJobExperiencesRequest createJobExperiencesRequest : jobExpRequest.getCreateJobExperiencesRequests()) {
            JobExperience jobExperience = jobExperienceHelper.createJobExperience(userId, createJobExperiencesRequest);
            jobExperiences.add(jobExperience);

            List<TeachingExperience> teachingExperiencesRes = new ArrayList<>();
            // Check Teaching Experience
            if (createJobExperiencesRequest.getTeachingExperiences() != null && !createJobExperiencesRequest.getTeachingExperiences().isEmpty()) {
                // Create Teaching Experience
                for (CreateTeachingExperienceRequest teachingExperienceRequest : createJobExperiencesRequest.getTeachingExperiences()) {
                    TeachingExperience teachingExperience = teachingExperienceHelper.createTeachingExperience(jobExperience.getId(), teachingExperienceRequest);
                    teachingExperiencesRes.add(teachingExperience);
                }
            }
            // test teachingExperiencesRes
            if (!teachingExperiencesRes.isEmpty()) {
                teachingExperiences.addAll(teachingExperiencesRes);
            }
            JobExperienceAndTeachingExperienceResponse jobExperienceAndTeachingExperienceResponse = new JobExperienceAndTeachingExperienceResponse(jobExperience,
                    teachingExperiencesRes);
            jobExperienceAndTeachingExperienceResponses.add(jobExperienceAndTeachingExperienceResponse);

        }

        if (!jobExperiences.isEmpty()) {
            jobExperienceService.saveAll(jobExperiences);
        }
        if (!teachingExperiences.isEmpty()) {
            teachingExperienceService.saveAll(teachingExperiences);
        }

        return responseUtil.successResponse(jobExperienceAndTeachingExperienceResponses);
    }

    /**
     * Get List Job Experiences API
     *
     * @param
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR, UserType.LEARNER})
    @GetMapping("/user/{user_id}")
    @Operation(summary = "Get List Job Experiences")
    public ResponseEntity<RestAPIResponse> getListJobExperiences(
            @PathVariable("user_id") String userId
    ) {
        // get list jobExperiences
        List<JobExperience> jobExperiences = jobExperienceService.getAllByUserIdOrderByCreatedDateDesc(userId);

        return responseUtil.successResponse(jobExperiences);
    }

    /**
     * Get Detail Job Experience API
     *
     * @param jobId
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR, UserType.LEARNER})
    @GetMapping(path = "/{job_id}")
    @Operation(summary = "get detail job experience")
    public ResponseEntity<RestAPIResponse> getDetailJobExperience(
            @PathVariable(name = "job_id") String jobId
    ) {
        // get jobExperience
        JobExperience jobExperience = jobExperienceService.getById(jobId);
        Validator.notNull(jobExperience, RestAPIStatus.NOT_FOUND, APIStatusMessage.JOB_EXPERIENCE_NOT_FOUND);

        return responseUtil.successResponse(jobExperience);
    }

    /**
     * Get List JobExperience By UserId API
     *
     * @param userId
     * @return
     */
    @GetMapping(path = "/{user_id}/public")
    @Operation(summary = "get list job experience")
    public ResponseEntity<RestAPIResponse> getListJobExperienceByUserId(
            @PathVariable(name = "user_id") String userId
    ) {
        // get jobExperience
        List<JobExperience> jobExperience = jobExperienceService.getAllByUserId(userId);
        Validator.notNull(jobExperience, RestAPIStatus.NOT_FOUND, APIStatusMessage.JOB_EXPERIENCE_NOT_FOUND);

        return responseUtil.successResponse(jobExperience);
    }

    /**
     * Update JobExperience API
     *
     * @param authUser
     * @param jobId
     * @param updateJobExperienceRequest
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR})
    @PutMapping(path = "/{job_id}")
    @Operation(summary = "Update JobExperience")
    public ResponseEntity<RestAPIResponse> updateJobExperience(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @PathVariable(name = "job_id") String jobId,
            @RequestBody @Valid UpdateJobExperienceRequest updateJobExperienceRequest
    ) {
        // get user by id
        JobExperience jobExperience = jobExperienceService.getById(jobId);
        Validator.notNull(jobExperience, RestAPIStatus.NOT_FOUND, APIStatusMessage.JOB_EXPERIENCE_NOT_FOUND);

        if (authUser.getType().equals(UserType.INSTRUCTOR)) {
            if (!authUser.getId().equals(jobExperience.getUserId())) {
                throw new ApplicationException(RestAPIStatus.FORBIDDEN, APIStatusMessage.CAN_ONLY_UPDATE_YOUR_OWN_INFORMATION);
            }
        }
        // update jobExperience
        jobExperienceHelper.updateJobExperience(jobExperience, updateJobExperienceRequest);
        jobExperienceService.save(jobExperience);

        return responseUtil.successResponse(jobExperience);
    }

    /**
     * Delete Job Experience API
     *
     * @param ids
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR})
    @DeleteMapping()
    @Operation(summary = "delete job experience")
    public ResponseEntity<RestAPIResponse> deleteJobExperiences(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @RequestParam(name = "ids") List<String> ids
    ) {

        if (UserType.SYSTEM_ADMIN.equals(authUser.getType())) {
            jobExperienceService.deleteByIdIn(ids);
        } else {
            jobExperienceService.deleteByUserId(ids, authUser.getId());
        }

        return responseUtil.successResponse("ok");
    }
}

