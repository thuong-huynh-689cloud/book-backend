package com.cloud.secure.streaming.controllers;

import com.cloud.secure.streaming.common.enums.*;
import com.cloud.secure.streaming.common.utilities.RestAPIResponse;
import com.cloud.secure.streaming.common.utilities.RestAPIStatus;
import com.cloud.secure.streaming.common.utilities.Validator;
import com.cloud.secure.streaming.config.security.AuthorizeValidator;
import com.cloud.secure.streaming.controllers.helper.PointPackageHelper;
import com.cloud.secure.streaming.controllers.model.request.CreatePointPackageRequest;
import com.cloud.secure.streaming.controllers.model.request.UpdatePointPackageRequest;
import com.cloud.secure.streaming.entities.PointPackage;
import com.cloud.secure.streaming.services.PointPackageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author 689Cloud
 */
@RestController
@RequestMapping(ApiPath.POINT_PACKAGE_API)
@Slf4j
public class PointPackageController extends AbstractBaseController {

    final PointPackageService pointPackageService;

    final PointPackageHelper pointPackageHelper;

    public PointPackageController(PointPackageService pointPackageService, PointPackageHelper pointPackageHelper) {
        this.pointPackageService = pointPackageService;
        this.pointPackageHelper = pointPackageHelper;
    }

    /**
     * Create Point Package API
     *
     * @param createPointPackageRequest
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN})
    @PostMapping
    @Operation(summary = "Create Point Package")
    public ResponseEntity<RestAPIResponse> createPointPackage(
            @Valid @RequestBody CreatePointPackageRequest createPointPackageRequest
    ) {
        // get buyPoint
        PointPackage pointPackage = pointPackageHelper.createPointPackage(createPointPackageRequest);
        pointPackageService.save(pointPackage);

        return responseUtil.successResponse(pointPackage);
    }

    /**
     * Update Point Package APi
     *
     * @param updatePointPackageRequest
     * @param id
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN})
    @PutMapping(path = "/{id}")
    @Operation(summary = "Update Point Package")
    public ResponseEntity<RestAPIResponse> updatePointPackage(
            @Valid @RequestBody UpdatePointPackageRequest updatePointPackageRequest,
            @PathVariable("id") String id  // Point id
    ) {
        // get buyPoint
        PointPackage pointPackage = pointPackageService.getById(id);
        Validator.notNull(pointPackage, RestAPIStatus.NOT_FOUND, APIStatusMessage.POINT_NOT_FOUND);

        // update buyPoint
        pointPackage = pointPackageHelper.updatePointPackage(pointPackage, updatePointPackageRequest);
        pointPackageService.save(pointPackage);

        return responseUtil.successResponse(pointPackage);

    }

    /**
     * Get Detail Point Package API
     *
     * @param id
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR, UserType.LEARNER})
    @GetMapping(path = "/{id}")
    @Operation(summary = "Get Point Package")
    public ResponseEntity<RestAPIResponse> getDetailPointPackage(
            @PathVariable(name = "id") String id // Point id
    ) {
        // get Point
        PointPackage pointPackage = pointPackageService.getById(id);
        Validator.notNull(pointPackage, RestAPIStatus.NOT_FOUND, APIStatusMessage.POINT_NOT_FOUND);

        return responseUtil.successResponse(pointPackage);
    }

    /**
     * get List Point API
     *
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR, UserType.LEARNER})
    @GetMapping(path = "all")
    @Operation(summary = "Get List Point Package")
    public ResponseEntity<RestAPIResponse> getListPointPackage(

    ) {
        // get all Point
        List<PointPackage> pointPackages = pointPackageService.getAll();

        return responseUtil.successResponse(pointPackages);
    }

    /**
     * Delete Point API
     *
     * @param ids
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN})
    @DeleteMapping()
    @Operation(summary = "Delete Point Package")
    public ResponseEntity<RestAPIResponse> deletePointPackage(
            @RequestParam(name = "ids") List<String> ids // buyPoint ids
    ) {
        // delete Point
        pointPackageService.deleteAllByIdIn(ids);

        return responseUtil.successResponse("Ok");
    }
}
