package com.cloud.secure.streaming.controllers;

import com.cloud.secure.streaming.common.utilities.RestAPIResponse;
import com.cloud.secure.streaming.common.utilities.RestAPIStatus;
import com.cloud.secure.streaming.common.utilities.Validator;
import com.cloud.secure.streaming.config.security.AuthSession;
import com.cloud.secure.streaming.config.security.AuthUser;
import com.cloud.secure.streaming.controllers.helper.CardHelper;
import com.cloud.secure.streaming.controllers.model.request.CreateCardRequest;
import com.cloud.secure.streaming.controllers.model.request.UpdateCardRequest;
import com.cloud.secure.streaming.entities.Card;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Quy Duong
 */
@RestController
@RequestMapping(ApiPath.CARD_API)
public class CardController extends AbstractBaseController {
    final CardService cardService;
    final CardHelper cardHelper;

    public CardController(CardService cardService, CardHelper cardHelper) {
        this.cardService = cardService;
        this.cardHelper = cardHelper;
    }


    /**
     * Create Card API
     * @param createCardRequest
     * @param authUser
     * @return
     */

    @PostMapping
    @Operation(summary = "Create Card")
    public ResponseEntity<RestAPIResponse> createCard(
            @RequestBody CreateCardRequest createCardRequest,
            @Parameter(hidden = true) @AuthSession AuthUser authUser

    ) {
        // create card
        Card card = cardHelper.createCard(createCardRequest, authUser);
        cardService.saveCard(card);
        return responseUtil.successResponse(card);

    }


    /**
     *  get card IPA
     * @param id
     * @return
     */
    @GetMapping(path = ApiPath.ID)
    @Operation(summary = "Get Card")
    public ResponseEntity<RestAPIResponse> getCard(
            @PathVariable(name = "id") String id

    ) {
        // check id
        Card card = cardService.getById(id);
        Validator.notNull(card, RestAPIStatus.NOT_FOUND, "id card not found");

        return responseUtil.successResponse(card);
    }


    /**
     * Update Card API
     * @param id
     * @param updateCardRequest
     * @return
     */
    @PutMapping(path = ApiPath.ID)
    @Operation(summary = "Update Card")
    public ResponseEntity<RestAPIResponse> updateCard(
            @PathVariable(name = "id") String id,
            @Valid @RequestBody UpdateCardRequest updateCardRequest
    ) {

        // check id
        Card card = cardService.getById(id);
        Validator.notNull(card, RestAPIStatus.NOT_FOUND, "id card not found");
        // update card
        card = cardHelper.updateCard(card, updateCardRequest);
        cardService.saveCard(card);

        return responseUtil.successResponse(card);
    }


    /**
     *
     * Delete Card API
     * @param id
     * @return
     */
    @DeleteMapping(path = ApiPath.ID)
    @Operation(summary = "Delete Card")
    public ResponseEntity<RestAPIResponse> deleteCard(
            @PathVariable(name = "id") String id

    ) {
        // check id
        Card card = cardService.getById(id);
        Validator.notNull(card, RestAPIStatus.NOT_FOUND, "id card not found");
        cardService.deleteCard(card);

        return responseUtil.successResponse("ok");
    }
}
