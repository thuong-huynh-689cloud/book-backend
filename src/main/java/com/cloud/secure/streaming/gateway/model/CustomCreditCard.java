package com.cloud.secure.streaming.gateway.model;

import com.cloud.secure.streaming.common.enums.CardType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.stripe.model.Token;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.USE_DEFAULTS)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomCreditCard {
    private String cardId;
    private String cardHolder;
    private Long expirationMonth;
    private Long expirationYear;
    private CardType cardType;
    private String addressCity;
    private String addressCountry;
    private String addressLine1;
    private String addressLine2;
    private String addressState;
    private String addressZip;
    private String last4;

    public CustomCreditCard(Token token) {

        this.cardId = token.getCard().getId();
        this.cardHolder = token.getCard().getName();
        this.expirationMonth = token.getCard().getExpMonth();
        this.expirationYear = token.getCard().getExpYear();

        // get card type
        String cardUserType = token.getCard().getBrand();
        this.cardType = (cardUserType != null) ? CardType.valueOf(cardUserType.toUpperCase()) : CardType.UNKNOWN;

        this.addressCity = token.getCard().getAddressCity();
        this.addressCountry = token.getCard().getAddressCountry();
        this.addressLine1 = token.getCard().getAddressLine1();
        this.addressLine2 = token.getCard().getAddressLine2();
        this.addressState = token.getCard().getAddressState();
        this.addressZip = token.getCard().getAddressZip();
        this.last4 = token.getCard().getLast4();
    }
}
