package com.cloud.secure.streaming.controllers.model.response;

import com.cloud.secure.streaming.common.enums.CardType;
import com.cloud.secure.streaming.entities.PointPackage;
import com.cloud.secure.streaming.entities.Transaction;
import com.cloud.secure.streaming.entities.User;
import com.stripe.model.Card;
import com.stripe.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {
    private String userId;

    private String transactionId;

    private String cardNumber; // stripe

    private String cardHolder; // stripe

    private String cardExpDate; // stripe

    private CardType cardType; // stripe

    private String status;

    private double point;

    private double price;

    public PaymentResponse(User user, Transaction transaction, PointPackage pointPackage){
        this.userId = user.getId();
        this.transactionId = transaction.getId();
        this.cardNumber = user.getCardNumber();
        this.cardHolder = user.getCardHolder();
        this.cardExpDate = user.getCardExpDate();
        this.cardType = user.getCardType();
        this.status = transaction.getChargeStatus();
        this.point = pointPackage.getPoint();
        this.price = pointPackage.getPrice();
    }
}
