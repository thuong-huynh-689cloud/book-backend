package com.cloud.secure.streaming.controllers.helper;

import com.cloud.secure.streaming.common.utilities.DateUtil;
import com.cloud.secure.streaming.common.utilities.UniqueID;
import com.cloud.secure.streaming.controllers.model.request.CreateBankInformationRequest;
import com.cloud.secure.streaming.controllers.model.request.UpdateBankInformationRequest;
import com.cloud.secure.streaming.entities.BankInformation;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class BankInformationHelper {
    public BankInformation createBankInformation(String id, CreateBankInformationRequest createBankInformationRequest) {
        BankInformation bankInformation = new BankInformation(
                UniqueID.getUUID(), // set ID
                createBankInformationRequest.getBankName(), // set Bank Name
                createBankInformationRequest.getBranchName(), // set Branch Name
                createBankInformationRequest.getAccountNumber(), // set Account Number
                createBankInformationRequest.getAccountHolder(), // set Account Holder
                id
        );
        // set created date
        bankInformation.setCreatedDate(DateUtil.convertToUTC(new Date()));

        return bankInformation;
    }

    public BankInformation updateBankInformation(BankInformation bankInformation,
                                                 UpdateBankInformationRequest updateBankInformationRequest
    ) {
        // check Bank Name
        if (updateBankInformationRequest.getBankName() != null && !updateBankInformationRequest.getBankName().trim().isEmpty()
                && !updateBankInformationRequest.getBankName().trim().equals(bankInformation.getBankName())) {
            bankInformation.setBankName(updateBankInformationRequest.getBankName().trim());
        }
        // check Branch Name
        if (updateBankInformationRequest.getBranchName() != null && !updateBankInformationRequest.getBranchName().trim().isEmpty()
                && !updateBankInformationRequest.getBranchName().trim().equals(bankInformation.getBranchName())) {
            bankInformation.setBranchName(updateBankInformationRequest.getBranchName().trim());
        }
        // check Account Number
        if (updateBankInformationRequest.getAccountNumber() != null && !updateBankInformationRequest.getAccountNumber().trim().isEmpty()
                && !updateBankInformationRequest.getAccountNumber().trim().equals(bankInformation.getAccountNumber())) {
            bankInformation.setAccountNumber(updateBankInformationRequest.getAccountNumber().trim());
        }
        // check Account Holder
        if (updateBankInformationRequest.getAccountHolder() != null && !updateBankInformationRequest.getAccountHolder().trim().isEmpty()
                && !updateBankInformationRequest.getAccountHolder().trim().equals(bankInformation.getAccountHolder())) {
            bankInformation.setAccountHolder(updateBankInformationRequest.getAccountHolder().trim());
        }
        return bankInformation;
    }
}
