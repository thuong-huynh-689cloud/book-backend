package com.cloud.secure.streaming.controllers;

import com.cloud.secure.streaming.common.enums.APIStatusMessage;
import com.cloud.secure.streaming.common.enums.UserType;
import com.cloud.secure.streaming.common.utilities.RestAPIResponse;
import com.cloud.secure.streaming.common.utilities.RestAPIStatus;
import com.cloud.secure.streaming.common.utilities.Validator;
import com.cloud.secure.streaming.config.security.AuthSession;
import com.cloud.secure.streaming.config.security.AuthUser;
import com.cloud.secure.streaming.config.security.AuthorizeValidator;
import com.cloud.secure.streaming.controllers.helper.ExchangeHelper;
import com.cloud.secure.streaming.controllers.model.request.UpdateExchangeRateRequest;
import com.cloud.secure.streaming.entities.ExchangeRate;
import com.cloud.secure.streaming.services.ExchangeRateService;
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
@RequestMapping(ApiPath.EXCHANGE_RATE_API)
@Slf4j
public class ExchangeRateController extends AbstractBaseController {

    final ExchangeRateService exchangeRateService;
    final ExchangeHelper exchangeHelper;

    public ExchangeRateController(ExchangeRateService exchangeRateService, ExchangeHelper exchangeHelper) {
        this.exchangeRateService = exchangeRateService;
        this.exchangeHelper = exchangeHelper;
    }

    /**
     * Update Exchange RateRequest API
     *
     * @param id
     * @param updateExchangeRateRequest
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN})
    @PutMapping(path = "/{id}")
    @Operation(summary = "Update ExchangeRate")
    public ResponseEntity<RestAPIResponse> updateExchangeRate(
            @PathVariable(name = "id") String id,
            @Valid @RequestBody UpdateExchangeRateRequest updateExchangeRateRequest
    ) {
        // check exchangeRate
        ExchangeRate exchangeRate = exchangeRateService.getById(id);
        Validator.notNull(exchangeRate, RestAPIStatus.NOT_FOUND, APIStatusMessage.EXCHANGE_RATE_NOT_FOUND);
        // update exchangeRate
        exchangeRate = exchangeHelper.updateExchangeRate(exchangeRate, updateExchangeRateRequest);
        exchangeRateService.save(exchangeRate);

        return responseUtil.successResponse(exchangeRate);
    }

    /**
     *   Get Detail ExchangeRate API
     *
     * @param authUser
     * @param id
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN})
    @GetMapping(path = "/{id}")
    @Operation(summary = "Get Detail Rate")
    public ResponseEntity<RestAPIResponse> getDetailRate(
            @Parameter(hidden = true) @AuthSession AuthUser authUser,
            @PathVariable(name = "id") String id
    ) {
        // get exchangeRate
        ExchangeRate exchangeRate = exchangeRateService.getById(id);
        Validator.notNull(exchangeRate, RestAPIStatus.NOT_FOUND, APIStatusMessage.EXCHANGE_RATE_NOT_FOUND);

        return responseUtil.successResponse(exchangeRate);
    }

    /**
     * Get List ExchangeRate API
     *
     * @return
     */
    @AuthorizeValidator({UserType.SYSTEM_ADMIN, UserType.INSTRUCTOR, UserType.LEARNER})
    @GetMapping(path = "/all")
    @Operation(summary = "Get List Rate")
    public ResponseEntity<RestAPIResponse> getListRate(

    ) {
        // get all exchangeRates
        List<ExchangeRate> exchangeRates = exchangeRateService.getAllByOrderByCreatedDateDesc();

        return responseUtil.successResponse(exchangeRates);
    }
}

