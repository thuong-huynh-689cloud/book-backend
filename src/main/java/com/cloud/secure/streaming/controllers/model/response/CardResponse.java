package com.cloud.secure.streaming.controllers.model.response;

import com.cloud.secure.streaming.common.enums.CardType;
import com.cloud.secure.streaming.entities.User;

public class CardResponse {

    private String userId;

    private String cardId;

    private String cardNumber; // stripe

    private String cardHolder; // stripe

    private String cardExpDate; // stripe

    private CardType cardType; // stripe

    public CardResponse(User user) {
        this.userId = user.getId();
        this.cardId = user.getCardId();
        this.cardNumber = user.getCardNumber();
        this.cardHolder = user.getCardHolder();
        this.cardExpDate = user.getCardExpDate();
        this.cardType = user.getCardType();

    }
}
