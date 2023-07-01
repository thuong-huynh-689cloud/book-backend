package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.common.enums.ResetCodeType;
import com.cloud.secure.streaming.entities.ResetCode;

public interface ResetCodeService {

    ResetCode getByIdAndType(String verifyKey, ResetCodeType type);

    ResetCode saveResetCode(ResetCode resetCode);

    void deleteResetCode(ResetCode resetCode);

}
