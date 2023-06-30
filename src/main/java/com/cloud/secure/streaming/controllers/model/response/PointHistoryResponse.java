package com.cloud.secure.streaming.controllers.model.response;

import com.cloud.secure.streaming.entities.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointHistoryResponse {

    private String transactionId;
    private String student;
    private String email;
    private Date createdDate;
    private Double pointPackage;
    private Double price;

    public PointHistoryResponse(PointHistory  pointHistory, User user ,PointPackage pointPackage) {
        this.transactionId = pointHistory.getTransactionId();
        this.student = user.getName();
        this.email = user.getEmail();
        this.createdDate = pointHistory.getCreatedDate();
        this.pointPackage = pointPackage.getPoint();
        this.price = pointPackage.getPrice();
    }
}
