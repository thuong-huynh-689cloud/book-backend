package com.cloud.secure.streaming.controllers.helper;

import com.cloud.secure.streaming.common.enums.CodeType;
import com.cloud.secure.streaming.common.utilities.DateUtil;
import com.cloud.secure.streaming.common.utilities.UniqueID;
import com.cloud.secure.streaming.entities.Code;
import com.cloud.secure.streaming.entities.User;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.TimeZone;

/**
 * @author 689Cloud
 */
@Component
public class CodeHelper {

    /**
     * create Code
     *
     * @param user
     * @return
     */
    public Code createCode(User user, CodeType codeType) {
        Code code = new Code();
        // Generate ID
        code.setId(UniqueID.getUUID());
        System.out.println(new Date());
        //set Expire Date
        code.setExpireDate(DateUtil.addMinutesToJavaUtilDate(new Date(), 30));
        //set type
        code.setType(codeType);
        //set user id
        code.setUserId(user.getId());
        return code;
    }

    /**
     * Create verify code
     *
     * @param user
     * @param codeType
     * @return
     */
    public Code createVerifyCode(User user, CodeType codeType) {
        Code code = new Code();
        // Generate ID
        code.setId(UniqueID.getUUID());
        // set Created Date
        code.setCreatedDate(DateUtil.convertToUTC(new Date()));
        //set Expire Date
        code.setExpireDate(DateUtil.addDate(new Date(), TimeZone.getDefault(), 1));
        //set type
        code.setType(codeType);
        //set user id
        code.setUserId(user.getId());
        return code;
    }
}
