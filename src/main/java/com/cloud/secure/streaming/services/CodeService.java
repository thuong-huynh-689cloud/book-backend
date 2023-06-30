package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.common.enums.CodeType;
import com.cloud.secure.streaming.entities.Code;

import java.util.Date;

/**
 * @author 689Cloud
 */
public interface CodeService {

    Code save(Code code);

    void delete(Code code);

    Code getById(String Id);

    int saveCodeCheckUserId(Code code, String userId);

    void deleteByUserIdAndType(String userId, CodeType type);

    Code getByIdAndType(String id, CodeType type);

    int deleteCheckDateAndType(String id, Date date, CodeType type);

}
