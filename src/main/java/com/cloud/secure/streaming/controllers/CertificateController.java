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
import com.cloud.secure.streaming.controllers.helper.CertificateHelper;
import com.cloud.secure.streaming.controllers.model.request.CreateCertificateRequest;
import com.cloud.secure.streaming.controllers.model.request.UpdateCertificateRequest;
import com.cloud.secure.streaming.entities.Certificate;
import com.cloud.secure.streaming.entities.User;
import com.cloud.secure.streaming.services.CertificateService;
import com.cloud.secure.streaming.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author 689Cloud
 */
@RestController
@RequestMapping(ApiPath.CERTIFICATE_API)
@Slf4j
public class CertificateController extends AbstractBaseController {

    final CertificateService certificateService;
    final CertificateHelper certificateHelper;
    final UserService userService;

    public CertificateController(CertificateService certificateService, CertificateHelper certificateHelper, UserService userService) {
        this.certificateService = certificateService;
        this.certificateHelper = certificateHelper;
        this.userService = userService;
    }

    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR})
    @PostMapping()
    @Operation(summary = "Create Certificate")
    public ResponseEntity<RestAPIResponse> createCertificate(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @RequestBody @Valid CreateCertificateRequest createCertificateRequest
    ) {
        String userId = null;

        if (authUser.getType().equals(UserType.SYSTEM_ADMIN)) {
            userId = createCertificateRequest.getUserId();
            User user = userService.getById(userId);
            Validator.notNull(user, RestAPIStatus.NOT_FOUND, APIStatusMessage.USER_NOT_FOUND);
        }
        if (authUser.getType().equals(UserType.INSTRUCTOR)) {
            userId = authUser.getId();
        }
        // create certificate
        Certificate certificate = certificateHelper.createCertificate(userId, createCertificateRequest);

        certificateService.save(certificate);

        return responseUtil.successResponse(certificate);
    }

    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR})
    @PutMapping(path = "/{id}")
    @Operation(summary = "Save All Certificate")
    public ResponseEntity<RestAPIResponse> updateCertificate(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @RequestBody @Valid UpdateCertificateRequest updateCertificateRequest,
            @PathVariable("id") String id  // course id
    ) {
        // get certificate
        Certificate certificate = certificateService.getById(id);
        Validator.notNull(certificate, RestAPIStatus.NOT_FOUND, APIStatusMessage.CERTIFICATE_NOT_FOUND);

        if (authUser.getType().equals(UserType.INSTRUCTOR)) {
            if (!authUser.getId().equals(certificate.getUserId())) {
                throw new ApplicationException(RestAPIStatus.FORBIDDEN, APIStatusMessage.CAN_ONLY_UPDATE_YOUR_OWN_INFORMATION);
            }
        }
        certificate = certificateHelper.updateCertificate(certificate, updateCertificateRequest);

        certificateService.save(certificate);

        return responseUtil.successResponse(certificate);
    }


    /**
     * Get List Certificate API
     *
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR})
    @GetMapping("/{id}")
    @Operation(summary = "Get Detail Certificate")
    public ResponseEntity<RestAPIResponse> getDetailCertificate(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @PathVariable("id") String id  // certificateId
    ) {
        Certificate certificate = certificateService.getById(id);
        Validator.notNull(certificate, RestAPIStatus.NOT_FOUND, APIStatusMessage.CERTIFICATE_NOT_FOUND);

        if (authUser.getType().equals(UserType.INSTRUCTOR)) {
            if (!authUser.getId().equals(certificate.getUserId())) {
                throw new ApplicationException(RestAPIStatus.FORBIDDEN, APIStatusMessage.CAN_ONLY_UPDATE_YOUR_OWN_INFORMATION);
            }
        }
        return responseUtil.successResponse(certificate);
    }

    /**
     * Get List Certificate API
     *
     * @param authUser
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR})
    @GetMapping(path = "/user/{user_id}")
    @Operation(summary = "Get List Certificate")
    public ResponseEntity<RestAPIResponse> getListCertificate(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @PathVariable("user_id") String userId
    ) {
        if (authUser.getType().equals(UserType.INSTRUCTOR)) {
            if (!authUser.getId().equals(userId)) {
                throw new ApplicationException(RestAPIStatus.FORBIDDEN, APIStatusMessage.CAN_ONLY_VIEW_YOUR_OWN_INFORMATION);
            }
        }

        List<Certificate> certificates = certificateService.getAllByUserIdOrderByCreatedDateDesc(userId);
        return responseUtil.successResponse(certificates);
    }

    /**
     * Delete Certificates API
     *
     * @param ids
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR})
    @DeleteMapping
    @Operation(summary = "Delete Certificates")
    public ResponseEntity<RestAPIResponse> deleteCertificates(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @RequestParam(name = "ids") List<String> ids
    ) {
        if (UserType.SYSTEM_ADMIN.equals(authUser.getType())) {
            certificateService.deleteByIdIn(ids);
        }
        else {
            certificateService.deleteByUserId(ids, authUser.getId());
        }

        return responseUtil.successResponse("Ok");
    }
}
