package com.cloud.secure.streaming.controllers;

import com.cloud.secure.streaming.common.enums.APIStatusMessage;
import com.cloud.secure.streaming.common.enums.UserType;
import com.cloud.secure.streaming.common.utilities.RestAPIResponse;
import com.cloud.secure.streaming.common.utilities.RestAPIStatus;
import com.cloud.secure.streaming.common.utilities.Validator;
import com.cloud.secure.streaming.config.security.AuthSession;
import com.cloud.secure.streaming.config.security.AuthUser;
import com.cloud.secure.streaming.config.security.AuthorizeValidator;
import com.cloud.secure.streaming.controllers.helper.RateHelper;
import com.cloud.secure.streaming.controllers.model.request.UpdateRateRequest;
import com.cloud.secure.streaming.entities.Rate;
import com.cloud.secure.streaming.services.RateService;
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
@RequestMapping(ApiPath.RATE_API)
@Slf4j
public class RateController extends AbstractBaseController {
    final RateService rateService;
    final RateHelper rateHelper;

    public RateController(RateService rateService, RateHelper rateHelper) {
        this.rateService = rateService;
        this.rateHelper = rateHelper;
    }

    /**
     * Update Rate Request API
     *
     * @param id
     * @param updateRateRequest
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN})
    @PutMapping(path = "/{id}")
    @Operation(summary = "Update Rate")
    public ResponseEntity<RestAPIResponse> updateRate(
            @PathVariable(name = "id") String id,
            @Valid @RequestBody UpdateRateRequest updateRateRequest
    ) {
        // check rate
        Rate rate = rateService.getById(id);
        Validator.notNull(rate, RestAPIStatus.NOT_FOUND, APIStatusMessage.RATE_NOT_FOUND);
        // update rate
        rate = rateHelper.updateRate(rate, updateRateRequest);
        rateService.save(rate);

        return responseUtil.successResponse(rate);
    }

    /**
     * Get Detail Rate API
     *
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN})
    @GetMapping(path = "/{id}")
    @Operation(summary = "Get Detail Rate")
    public ResponseEntity<RestAPIResponse> getDetailRate(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @PathVariable(name = "id") String id
    ) {
        // get rate
        Rate rate = rateService.getById(id);
        Validator.notNull(rate,RestAPIStatus.NOT_FOUND,APIStatusMessage.RATE_NOT_FOUND);

        return responseUtil.successResponse(rate);

    }

    /**
     *   Get List Rate API
     *
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR, UserType.LEARNER})
    @GetMapping(path = "/all")
    @Operation(summary = "Get List Rate")
    public ResponseEntity<RestAPIResponse> getListRate(

    ) {
        // get all rate
        List<Rate> rates = rateService.getAllByOrderByCreatedDateDesc();

        return responseUtil.successResponse(rates);
    }
}
