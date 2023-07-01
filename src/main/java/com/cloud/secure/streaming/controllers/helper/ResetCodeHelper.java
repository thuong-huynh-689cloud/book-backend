package com.cloud.secure.streaming.controllers.helper;

import com.cloud.secure.streaming.common.enums.ResetCodeType;
import com.cloud.secure.streaming.common.utilities.DateUtil;
import com.cloud.secure.streaming.common.utilities.UniqueID;
import com.cloud.secure.streaming.entities.ResetCode;
import com.cloud.secure.streaming.entities.User;
import org.springframework.stereotype.Component;


import java.util.Date;
import java.util.TimeZone;

@Component

public class ResetCodeHelper {

    /**
     *  Create new Reset Code
     *
     * @param user
     * @return
     */
    public ResetCode createResetCode (User user){
        ResetCode resetCode = new ResetCode();
        // Generate ID
        resetCode.setId(UniqueID.getUUID());
        // set Created Date
        resetCode.setCreatedDate(DateUtil.convertToUTC(new Date()));
        //set Expire Date 1 day
        resetCode.setExpireDate(DateUtil.addDate(new Date(), TimeZone.getDefault(),1));
        //set type
        resetCode.setType(ResetCodeType.RESET_PASSWORD);
        //set user id
        resetCode.setUserId(user.getId());

        return resetCode;

    }
    public ResetCode createSignUp ( User user){
        ResetCode resetCode = new ResetCode();
        // Generate ID
        resetCode.setId(UniqueID.getUUID());
        // set Created Date
        resetCode.setCreatedDate(DateUtil.convertToUTC(new Date()));
        //set Expire Date 1 day
        resetCode.setExpireDate(DateUtil.addDate(new Date(), TimeZone.getDefault(),1));
        //set type
        resetCode.setType(ResetCodeType.VERIFY_ACCOUNT);
        //set user id
        resetCode.setUserId(user.getId());

        return resetCode;

    }
}
