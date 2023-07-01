package com.cloud.secure.streaming.controllers.helper;

import com.cloud.secure.streaming.common.utilities.UniqueID;
import com.cloud.secure.streaming.config.security.AuthUser;
import com.cloud.secure.streaming.entities.Card;
import org.springframework.stereotype.Component;

@Component
public class CardHelper {

    public Card createCard(CreateCardRequest createCardRequest, AuthUser authUser) {
        Card card = new Card();
        card.setId(UniqueID.getUUID());
        card.setUserId(authUser.getId());
        card.setCardNumber(createCardRequest.getCardNumber());
        card.setCardName(createCardRequest.getCardName());
        card.setExpiredDate(createCardRequest.getExpiredDate());
        card.setType(createCardRequest.getType());
        card.setMoneyInCard(createCardRequest.getMoneyInCard());

        return card;
    }
    public Card updateCard(Card card, UpdateCardRequest updateCardRequest){
        if (updateCardRequest.getCardNumber()!=null&& !updateCardRequest.getCardNumber().trim().isEmpty()&&
        !updateCardRequest.getCardNumber().trim().equals(card.getCardNumber())){
            card.setCardNumber(updateCardRequest.getCardNumber());
        }
        if (updateCardRequest.getCardName()!=null&& !updateCardRequest.getCardName().trim().isEmpty()&&
        !updateCardRequest.getCardName().trim().equals(card.getCardName())){
            card.setCardName(updateCardRequest.getCardName());
        }
        if (updateCardRequest.getExpiredDate()!=null){
            card.setExpiredDate(updateCardRequest.getExpiredDate());
        }

        if (updateCardRequest.getType() !=null){
            card.setType(updateCardRequest.getType());
        }
        return card;
    }
}
