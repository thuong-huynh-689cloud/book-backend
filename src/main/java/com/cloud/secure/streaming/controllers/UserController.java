package com.cloud.secure.streaming.controllers;

import com.cloud.secure.streaming.common.enums.*;
import com.cloud.secure.streaming.common.exceptions.ApplicationException;
import com.cloud.secure.streaming.common.utilities.*;
import com.cloud.secure.streaming.config.security.AuthSession;
import com.cloud.secure.streaming.config.security.AuthUser;
import com.cloud.secure.streaming.config.security.AuthorizeValidator;
import com.cloud.secure.streaming.controllers.helper.*;
import com.cloud.secure.streaming.controllers.model.request.*;
import com.cloud.secure.streaming.controllers.model.response.*;
import com.cloud.secure.streaming.entities.*;
import com.cloud.secure.streaming.services.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 689Cloud
 */
@RestController
@RequestMapping(ApiPath.USER_API)
@Slf4j
public class UserController extends AbstractBaseController {

    final UserService userService;
    final UserHelper userHelper;
    final UserLanguageService userLanguageService;
    final UserLanguageHelper userLanguageHelper;
    final JobExperienceService jobExperienceService;
    final JobExperienceHelper jobExperienceHelper;
    final TeachingExperienceService teachingExperienceService;
    final TeachingExperienceHelper teachingExperienceHelper;
    final CodeHelper codeHelper;
    final CodeService codeService;
    final EmailSenderHelper emailSenderHelper;
    final UploadHelper uploadHelper;
    final ApplicationConfigureValues appConfig;
    final SessionService sessionService;
    final SessionHelper sessionHelper;
    final UserAdminFeedbackService userAdminFeedbackService;
    final UserAdminFeedbackHelper userAdminFeedbackHelper;
    final OrdersService ordersService;
    final PointHistoryHelper pointHistoryHelper;
    final PointHistoryService pointHistoryService;
    final CourseService courseService;

    public UserController(UserService userService, UserHelper userHelper, UserLanguageService userLanguageService,
                          UserLanguageHelper userLanguageHelper, JobExperienceService jobExperienceService,
                          JobExperienceHelper jobExperienceHelper, TeachingExperienceService teachingExperienceService,
                          TeachingExperienceHelper teachingExperienceHelper, CodeHelper codeHelper, CodeService codeService,
                          EmailSenderHelper emailSenderHelper, UploadHelper uploadHelper, ApplicationConfigureValues appConfig,
                          SessionService sessionService, SessionHelper sessionHelper, UserAdminFeedbackService userAdminFeedbackService,
                          UserAdminFeedbackHelper userAdminFeedbackHelper, OrdersService ordersService, PointHistoryHelper pointHistoryHelper, PointHistoryService pointHistoryService, CourseService courseService) {
        this.userService = userService;
        this.userHelper = userHelper;
        this.userLanguageService = userLanguageService;
        this.userLanguageHelper = userLanguageHelper;
        this.jobExperienceService = jobExperienceService;
        this.jobExperienceHelper = jobExperienceHelper;
        this.teachingExperienceService = teachingExperienceService;
        this.teachingExperienceHelper = teachingExperienceHelper;
        this.codeHelper = codeHelper;
        this.codeService = codeService;
        this.emailSenderHelper = emailSenderHelper;
        this.uploadHelper = uploadHelper;
        this.appConfig = appConfig;
        this.sessionService = sessionService;
        this.sessionHelper = sessionHelper;
        this.userAdminFeedbackService = userAdminFeedbackService;
        this.userAdminFeedbackHelper = userAdminFeedbackHelper;
        this.ordersService = ordersService;
        this.pointHistoryHelper = pointHistoryHelper;
        this.pointHistoryService = pointHistoryService;
        this.courseService = courseService;
    }

    /**
     * Create User API
     *
     * @param createUserRequest
     * @return
     */
    @AuthorizeValidator(UserType.SYSTEM_ADMIN)
    @PostMapping()
    @Operation(summary = "Create User")
    public ResponseEntity<RestAPIResponse> createUser(
            @Valid @RequestBody CreateUserRequest createUserRequest
    ) {
        // Validate Email
        Validator.mustMatch(createUserRequest.getEmail(), Constant.EMAIL_PATTERN, RestAPIStatus.BAD_PARAMS,
                APIStatusMessage.INVALID_EMAIL_FORMAT);
        // check email not exist in database
        User user = userService.getByEmailAndStatusAndType(createUserRequest.getEmail(), AppStatus.ACTIVE,
                createUserRequest.getType());
        Validator.mustNull(user, RestAPIStatus.EXISTED, APIStatusMessage.EMAIL_EXISTED);
        // encrypt password
        String passwordRandom = "";
        String passwordMD5 = "";
        if (createUserRequest.getNewPassword() != null && !createUserRequest.getNewPassword().isEmpty() &&
                createUserRequest.getConfirmNewPassword() != null && !createUserRequest.getConfirmNewPassword().isEmpty()) {
            // check password matching
            if (!createUserRequest.getNewPassword().equals(createUserRequest.getConfirmNewPassword())) {
                throw new ApplicationException(RestAPIStatus.FAIL, APIStatusMessage.PASSWORD_DOES_NOT_MATCH);
            }
            passwordMD5 = createUserRequest.getNewPassword();
        } else {
            // generate password
            passwordRandom = PasswordGenerator.generatePassword(8);
            // encrypt password md5
            try {
                passwordMD5 = AppUtil.encryptMD5(passwordRandom);
            } catch (NoSuchAlgorithmException e) {
                throw new ApplicationException(RestAPIStatus.CANNOT_ENCRYPT_RANDOM_PASSWORD);
            }
        }
        if ((createUserRequest.getPhone() != null && !createUserRequest.getPhone().isEmpty()
                && (createUserRequest.getPhoneDialCode() == null || "".equals(createUserRequest.getPhoneDialCode())))) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, APIStatusMessage.PHONE_DIAL_CODE_MUST_NOT_BE_NULL);
        }
        //create user
        user = userHelper.createUser(createUserRequest, passwordEncoder, passwordMD5);

        List<UserLanguage> userLanguages = new ArrayList<>();
        // Check Language
        if (createUserRequest.getLanguages() != null && !createUserRequest.getLanguages().isEmpty()) {
            // Create UserLanguage
            userLanguages = userLanguageHelper.createUserLanguages(user.getId(), createUserRequest.getLanguages());
        }
        // add Job
        List<JobExperience> jobExperiences = new ArrayList<>();
        // add teaching
        List<TeachingExperience> teachingExperiences = new ArrayList<>();
        List<JobExperienceAndTeachingExperienceResponse> jobExperienceAndTeachingExperienceResponses = new ArrayList<>();
        // Check jobExperiences
        if (createUserRequest.getJobExperiences() != null && !createUserRequest.getJobExperiences().isEmpty()) {
            // Create Job Experiences
            for (CreateJobExperiencesRequest createJobExperiencesRequest : createUserRequest.getJobExperiences()) {
                JobExperience jobExperience = jobExperienceHelper.createJobExperience(user.getId(), createJobExperiencesRequest);
                jobExperiences.add(jobExperience);

                List<TeachingExperience> teachingExperiencesRes = new ArrayList<>();
                // Check Teaching Experience
                if (createJobExperiencesRequest.getTeachingExperiences() != null &&
                        !createJobExperiencesRequest.getTeachingExperiences().isEmpty()) {
                    // Create Teaching Experience
                    for (CreateTeachingExperienceRequest teachingExperienceRequest : createJobExperiencesRequest.getTeachingExperiences()) {
                        TeachingExperience teachingExperience = teachingExperienceHelper.createTeachingExperience(jobExperience.getId(),
                                teachingExperienceRequest);
                        teachingExperiencesRes.add(teachingExperience);
                    }
                }
                if (!teachingExperiencesRes.isEmpty()) {
                    teachingExperiences.addAll(teachingExperiencesRes);
                }
                JobExperienceAndTeachingExperienceResponse jobExperienceAndTeachingExperienceResponse = new JobExperienceAndTeachingExperienceResponse(jobExperience,
                        teachingExperiencesRes);
                jobExperienceAndTeachingExperienceResponses.add(jobExperienceAndTeachingExperienceResponse);
            }
        }
        //save user
        userService.save(user);
        // save UserLanguage
        if (!userLanguages.isEmpty()) {
            userLanguageService.saveAll(userLanguages);
        }
        // save jobExperiences
        if (!jobExperiences.isEmpty()) {
            jobExperienceService.saveAll(jobExperiences);
        }
        // save teachingExperiences
        if (!teachingExperiences.isEmpty()) {
            teachingExperienceService.saveAll(teachingExperiences);
        }
        // send email password Raw
        emailSenderHelper.sendEmailCreateUser(user.getEmail(), user.getName(),
                passwordRandom, createUserRequest.getNewPassword(), createUserRequest.getLanguageSendMail(), user.getType());

        return responseUtil.successResponse(new UserResponseAndJob(user, userLanguages, jobExperienceAndTeachingExperienceResponses));
    }

    /**
     * Sign Up Instructor API
     *
     * @param createSignUpInstructorRequest
     * @return
     */
    @PostMapping(path = "/sign-up/instructor")
    @Operation(summary = "Sign Up Instructor")
    public ResponseEntity<RestAPIResponse> signUpInstructor(
            @Valid @RequestBody CreateSignUpInstructorRequest createSignUpInstructorRequest
    ) {
        // Validate Email
        Validator.mustMatch(createSignUpInstructorRequest.getEmail(), Constant.EMAIL_PATTERN, RestAPIStatus.BAD_PARAMS,
                APIStatusMessage.INVALID_EMAIL_FORMAT);
        // check password matching
        if (!createSignUpInstructorRequest.getNewPassword().equals(createSignUpInstructorRequest.getConfirmNewPassword())) {
            throw new ApplicationException(RestAPIStatus.FAIL, APIStatusMessage.PASSWORD_DOES_NOT_MATCH);
        }
        if ((createSignUpInstructorRequest.getPhone() != null && !createSignUpInstructorRequest.getPhone().isEmpty()
                && (createSignUpInstructorRequest.getPhoneDialCode() == null || "".equals(createSignUpInstructorRequest.getPhoneDialCode())))) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, APIStatusMessage.PHONE_DIAL_CODE_MUST_NOT_BE_NULL);
        }
        User user = userService.getByEmailAndStatusAndType(createSignUpInstructorRequest.getEmail(), AppStatus.ACTIVE, UserType.INSTRUCTOR);
        Validator.mustNull(user, RestAPIStatus.EXISTED, APIStatusMessage.EMAIL_EXISTED);

        //create user
        user = userHelper.createSignUpInstructor(createSignUpInstructorRequest, passwordEncoder, UserType.INSTRUCTOR);

        //save user
        userService.save(user);

        // send email
        emailSenderHelper.sendEmailSignUpAccount(user.getEmail(), user.getName(), createSignUpInstructorRequest.getLanguageSendMail(), user.getType());

        // get zoneId
        ZoneId zoneId = DateUtil.getZoneId(createSignUpInstructorRequest.getZoneId());
        // create session
        Session session = sessionHelper.createSession(user, false, zoneId);
        sessionService.save(session);

        return responseUtil.successResponse(session);
    }

    /**
     * Sign Up Leaner API
     *
     * @param createSignUpLeanerRequest
     * @return
     */
    @PostMapping(path = "/sign-up/learner")
    @Operation(summary = "Sign Up Learner")
    public ResponseEntity<RestAPIResponse> signUpLearner(
            @Valid @RequestBody CreateSignUpLeanerRequest createSignUpLeanerRequest
    ) {
        // check account number
        Validator.mustMatch(createSignUpLeanerRequest.getEmail(), Constant.EMAIL_PATTERN, RestAPIStatus.BAD_PARAMS,
                APIStatusMessage.INVALID_EMAIL_FORMAT);
        // check password matching
        if (!createSignUpLeanerRequest.getNewPassword().equals(createSignUpLeanerRequest.getConfirmNewPassword())) {
            throw new ApplicationException(RestAPIStatus.FAIL, APIStatusMessage.PASSWORD_DOES_NOT_MATCH);
        }
        if ((createSignUpLeanerRequest.getPhone() != null && !createSignUpLeanerRequest.getPhone().isEmpty()
                && (createSignUpLeanerRequest.getPhoneDialCode() == null || "".equals(createSignUpLeanerRequest.getPhoneDialCode())))) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, APIStatusMessage.PHONE_DIAL_CODE_MUST_NOT_BE_NULL);
        }
        // get user
        User user = userService.getByEmailAndStatusAndType(createSignUpLeanerRequest.getEmail(), AppStatus.ACTIVE, UserType.LEARNER);
        Validator.mustNull(user, RestAPIStatus.EXISTED, APIStatusMessage.EMAIL_EXISTED);

        user = userHelper.createSignUpLeaner(createSignUpLeanerRequest, passwordEncoder, UserType.LEARNER);

        //Create user
        userService.save(user);

        // send email activate account
        emailSenderHelper.sendEmailSignUpAccount(user.getEmail(), user.getName(), createSignUpLeanerRequest.getLanguageSendMail(), user.getType());
        // get zoneId
        ZoneId zoneId = DateUtil.getZoneId(createSignUpLeanerRequest.getZoneId());
        // create session
        Session session = sessionHelper.createSession(user, false, zoneId);
        sessionService.save(session);

        return responseUtil.successResponse(session);
    }

    /**
     * Get Detail User
     *
     * @param authUser
     * @param id
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR, UserType.LEARNER})
    @GetMapping(path = "/{id}")
    @Operation(summary = "Get User Detail")
    public ResponseEntity<RestAPIResponse> getDetailUser(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @PathVariable(name = "id") String id
    ) {
        UserResponse userResponse;
        User user = null;
        // Check Permission
        if (authUser.getType().equals(UserType.INSTRUCTOR) || authUser.getType().equals(UserType.LEARNER)) {

            if (!authUser.getId().equals(id)) {
                throw new ApplicationException(RestAPIStatus.FORBIDDEN, APIStatusMessage.CAN_ONLY_VIEW_YOUR_OWN_INFORMATION);
            }
            // Get User
            user = userService.getByIdAndStatus(id, AppStatus.ACTIVE);
            Validator.notNull(user, RestAPIStatus.NOT_FOUND, APIStatusMessage.USER_NOT_FOUND);
        } else {
            // Get User
            user = userService.getByIdAndStatus(id, AppStatus.ACTIVE);
            Validator.notNull(user, RestAPIStatus.NOT_FOUND, APIStatusMessage.USER_NOT_FOUND);
        }
        // get userLanguages
        List<UserLanguage> userLanguages = userLanguageService.getAllByUserIdAndStatus(id, AppStatus.ACTIVE);

        if (UserType.INSTRUCTOR.equals(user.getType())) {
            // count user enrol
            Long totalUserEnrol = ordersService.getAllByUserId(user.getId());

            userResponse = new UserResponse(user, userLanguages, totalUserEnrol);
        } else {

            userResponse = new UserResponse(user, userLanguages);
        }

        return responseUtil.successResponse(userResponse);
    }

    /**
     * \
     *
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN})
    @GetMapping("/types")
    @Operation(summary = "Get List By Type")
    public ResponseEntity<RestAPIResponse> getListByType(
            @RequestParam(name = "user_types") List<UserType> userTypes

    ) {
        List<User> users = userService.getAllByTypeInAndStatusOrderByCreatedDateDesc(userTypes, AppStatus.ACTIVE);

        return responseUtil.successResponse(users);
    }

    /**
     * Get paging users
     *
     * @param searchKey
     * @param sortFieldUser
     * @param sortDirection
     * @param statuses
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @AuthorizeValidator(UserType.SYSTEM_ADMIN)
    @GetMapping
    @Operation(summary = "Get paging users")
    public ResponseEntity<RestAPIResponse> getPagingUsers(
            @RequestParam(name = "search_key", required = false, defaultValue = "") String searchKey,
            @RequestParam(name = "sort_field", required = false, defaultValue = "createdDate") SortFieldUser
                    sortFieldUser,
            @RequestParam(value = "sort_direction", required = false, defaultValue = "ASC") SortDirection
                    sortDirection, // ASC or DESC
            @RequestParam(name = "type", required = false, defaultValue = "") List<UserType> type,
            @RequestParam(name = "statuses", required = false) List<AppStatus> statuses, // ACTIVE,INACTIVE,PENDING
            @RequestParam(name = "page_number", required = false, defaultValue = "1") int pageNumber,
            @RequestParam(name = "page_size", required = false, defaultValue = "10") int pageSize
    ) {
        List<UserResponse> userResponse = new ArrayList<>();

        // validate page number and page size
        Validator.mustGreaterThanOrEqual(1, RestAPIStatus.BAD_REQUEST,
                APIStatusMessage.INVALID_PAGE_NUMBER_OR_PAGE_SIZE, pageNumber, pageSize);
        // check statuses
        if (statuses == null || statuses.isEmpty()) {
            statuses = Arrays.asList(AppStatus.values());
        }
        // check type
        if (type == null || type.isEmpty()) {
            type = Arrays.asList(UserType.values());
        }
        // get page user
        Page<User> userPage = userService.getPageUser(searchKey, sortFieldUser, sortDirection, type, statuses, pageNumber, pageSize);
        // get list language
        for (User user : userPage) {
            List<UserLanguage> userLanguage = userLanguageService.getAllByUserIdAndStatus(user.getId(), AppStatus.ACTIVE);

            userResponse.add(new UserResponse(user, userLanguage));
        }
        String pw = PasswordGenerator.generatePassword(8);
        System.out.println(pw);
        return responseUtil.successResponse((new PagingResponse(userResponse, userPage)));
    }

    /**
     * Update User
     *
     * @param authUser
     * @param id
     * @param updateUserRequest
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR, UserType.LEARNER})
    @PutMapping(path = "/{id}")
    @Operation(summary = "Update User")
    public ResponseEntity<RestAPIResponse> updateUser(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @PathVariable("id") String id,
            @RequestBody @Valid UpdateUserRequest updateUserRequest
    ) {
        // get user by id
        User user = userService.getByIdAndStatus(id, AppStatus.ACTIVE);
        Validator.notNull(user, RestAPIStatus.NOT_FOUND, APIStatusMessage.USER_NOT_FOUND);

        if (authUser.getType().equals(UserType.INSTRUCTOR) || authUser.getType().equals(UserType.LEARNER)) {
            if (!authUser.getId().equals(id)) {
                throw new ApplicationException(RestAPIStatus.FORBIDDEN, APIStatusMessage.CAN_ONLY_UPDATE_YOUR_OWN_INFORMATION);
            }
        }
        // update user
        userHelper.updateUser(user, updateUserRequest);
        // update userLanguage
        List<UserLanguage> userLanguageList = new ArrayList<>();
        List<Language> languageListRemove = new ArrayList<>();
        // check languages
        if (updateUserRequest.getLanguages() != null && !updateUserRequest.getLanguages().isEmpty()) {
            // get UserLanguage and set UserLanguage
            List<UserLanguage> userLanguages = userLanguageService.getAllByUserIdAndStatus(user.getId(), AppStatus.ACTIVE);
            if (userLanguages.isEmpty()) {
                new HashSet<>(updateUserRequest.getLanguages()).forEach(language -> {
                    UserLanguage userLanguage = new UserLanguage();
                    userLanguage.setId(UniqueID.getUUID());
                    userLanguage.setUserId(user.getId());
                    userLanguage.setLanguage(language);
                    userLanguage.setStatus(AppStatus.ACTIVE);

                    userLanguageList.add(userLanguage);
                });
            } else {
                Set<Language> languageExists = userLanguages.stream().map(UserLanguage::getLanguage).collect(Collectors.toSet());
                Set<Language> languageSaves = new HashSet<>(updateUserRequest.getLanguages());
                languageSaves.removeAll(languageExists);
                if (!languageSaves.isEmpty()) {
                    languageSaves.forEach(language -> {
                        UserLanguage userLanguage = new UserLanguage();
                        userLanguage.setId(UniqueID.getUUID());
                        userLanguage.setUserId(user.getId());
                        userLanguage.setLanguage(language);
                        userLanguage.setStatus(AppStatus.ACTIVE);

                        userLanguageList.add(userLanguage);
                    });
                }
                // remove language Exists
                languageExists.removeAll(updateUserRequest.getLanguages());

                if (!languageExists.isEmpty()) {
                    languageListRemove.addAll(languageExists);
                }
            }
        }
        // save User
        userService.save(user);
        // save UserLanguage
        if (!userLanguageList.isEmpty()) {
            userLanguageService.saveAll(userLanguageList);
        }
        if (!languageListRemove.isEmpty()) {
            userLanguageService.deleteByLanguageInAndUserId(languageListRemove, user.getId());
        }

        return responseUtil.successResponse(new UserResponse(user, userLanguageList));
    }

    /**
     * Delete User
     *
     * @param ids
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN})
    @DeleteMapping
    @Operation(summary = "Delete User")
    public ResponseEntity<RestAPIResponse> deleteUser(
            @RequestParam(name = "ids") List<String> ids
    ) {
        //get user
        List<User> users = userService.getAllByIdInAndStatus(ids, AppStatus.ACTIVE);
        if (users.size() != ids.size()) {
            throw new ApplicationException(RestAPIStatus.NOT_FOUND, APIStatusMessage.USER_NOT_FOUND);
        }
        // delete user
        userService.updateUserByIdInToInactive(ids);
        // delete Course
        courseService.updateByUserIdInToInactive(ids);

        return responseUtil.successResponse("Ok");
    }

    /**
     * Change Password For User API
     *
     * @param userId
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN})
    @PutMapping(path = "/{user_id}/change-password")
    @Operation(summary = "Change User Password")
    public ResponseEntity<RestAPIResponse> changeUserPassword(
            @PathVariable("user_id") String userId,

            @RequestParam Language language
    ) {
        //get user by authUser
        User user = userService.getByIdAndStatus(userId, AppStatus.ACTIVE);
        Validator.notNull(user, RestAPIStatus.NOT_FOUND, APIStatusMessage.USER_NOT_FOUND);
        if (user.getType().equals(UserType.SYSTEM_ADMIN)) {
            throw new ApplicationException(RestAPIStatus.FORBIDDEN, APIStatusMessage.CANNOT_CHANGE_THIS_USERS_PASSWORD);
        }
        String passwordRandom = "";
        String passwordMD5 = "";
        // generate password
        passwordRandom = PasswordGenerator.generatePassword(8);
        // encrypt password md5
        try {
            passwordMD5 = AppUtil.encryptMD5(passwordRandom);
        } catch (NoSuchAlgorithmException e) {
            throw new ApplicationException(RestAPIStatus.CANNOT_ENCRYPT_RANDOM_PASSWORD);
        }
        // Set new password
        String newSalt = AppUtil.generateSalt();
        user.setPasswordHash(passwordEncoder.encode(passwordMD5.trim().concat(newSalt)));
        user.setPasswordSalt(newSalt);
        userService.save(user);
        // send mail
        emailSenderHelper.sendEmailAdminChangePasswordUser(user.getName(), user.getEmail(), user
                .getType(), passwordRandom, language);
        return responseUtil.successResponse("OK");
    }

    /**
     * Add Point User API
     *
     * @param addPointUserRequest
     * @param userId
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN})
    @PutMapping(path = "/{user_id}/grant-point")
    @Operation(summary = "Grant Point User")
    public ResponseEntity<RestAPIResponse> addPointUser(
            @Valid @RequestBody AddPointUserRequest addPointUserRequest,
            @PathVariable("user_id") String userId //
    ) {
        //get user by authUser
        User user = userService.getByIdAndStatus(userId, AppStatus.ACTIVE);
        Validator.notNull(user, RestAPIStatus.NOT_FOUND, APIStatusMessage.USER_NOT_FOUND);

        double point = user.getTotalPoint() + addPointUserRequest.getTotalPoint();

        user.setTotalPoint(point);

        PointHistory pointHistory = pointHistoryHelper.createPointHistory(user.getId(), addPointUserRequest.getTotalPoint());

        pointHistoryService.save(pointHistory);
        userService.save(user);

        return responseUtil.successResponse(user);
    }

    /**
     * Update User Status API
     *
     * @param updateUserStatusRequest
     * @param id
     * @return
     */
    @AuthorizeValidator(UserType.SYSTEM_ADMIN)
    @PutMapping(path = "/{id}/status")
    @Operation(summary = "Update User Status")
    public ResponseEntity<RestAPIResponse> updateUserStatus(
            @RequestBody @Valid UpdateUserStatusRequest updateUserStatusRequest,
            @PathVariable("id") String id  // user id
    ) {
        //get user
        User user = userService.getById(id);
        Validator.notNull(user, RestAPIStatus.NOT_FOUND, APIStatusMessage.USER_NOT_FOUND);

        UserAdminFeedback userAdminFeedback = null;

        user.setStatus(updateUserStatusRequest.getStatus());

        if (AppStatus.REJECT.equals(updateUserStatusRequest.getStatus())) {
            // create admin feedback
            userAdminFeedback = userAdminFeedbackHelper.createUserAdminFeedback(user.getId(), updateUserStatusRequest.getFeedback());
            userAdminFeedbackService.save(userAdminFeedback);
        }
        if (AppStatus.ACTIVE.equals(updateUserStatusRequest.getStatus())) {
            //delete admin feedback
            userAdminFeedbackService.deleteAllByUserId(user.getId());
        }
        userService.save(user);

        return responseUtil.successResponse(new UserResponse(user, userAdminFeedback));
    }

    @GetMapping(path = "/{id}/public")
    @Operation(summary = "Get Detail User Public")
    public ResponseEntity<RestAPIResponse> getDetailUserPublic(
            @PathVariable(name = "id") String id // user id
    ) {
        UserResponse userResponse;

        //get user
        User user = userService.getByIdAndStatus(id, AppStatus.ACTIVE);
        Validator.notNull(user, RestAPIStatus.NOT_FOUND, APIStatusMessage.USER_NOT_FOUND);

        // get userLanguages
        List<UserLanguage> userLanguages = userLanguageService.getAllByUserIdAndStatus(id, AppStatus.ACTIVE);

        if (UserType.INSTRUCTOR.equals(user.getType())) {
            // count user enrol
            Long totalUserEnrol = ordersService.getAllByUserId(user.getId());

            userResponse = new UserResponse(user, userLanguages, totalUserEnrol);
        } else {

            userResponse = new UserResponse(user, userLanguages);
        }

        return responseUtil.successResponse(userResponse);
    }
}

