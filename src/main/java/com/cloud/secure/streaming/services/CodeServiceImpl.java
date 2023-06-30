package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.common.enums.CodeType;
import com.cloud.secure.streaming.entities.Code;
import com.cloud.secure.streaming.repositories.CodeRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author 689Cloud
 */
@Service
public class CodeServiceImpl implements CodeService {

    final CodeRepository codeRepository;

    public CodeServiceImpl(CodeRepository codeRepository) {
        this.codeRepository = codeRepository;
    }

    @Override
    public Code save(Code code) {
        return codeRepository.save(code);
    }

    @Override
    public void delete(Code code) {
        codeRepository.delete(code);
    }

    @Override
    public Code getById(String id) {
        return codeRepository.findById(id).orElse(null);
    }

    @Override
    public int saveCodeCheckUserId(Code code, String userId) {
        return codeRepository.insertCodeCheckUserId(code.getId(), code.getCreatedDate(), code.getExpireDate(),
                code.getType().name(), code.getUserId());
    }

    @Override
    public void deleteByUserIdAndType(String userId, CodeType type) {
        codeRepository.deleteByUserId(userId, type);
    }

    @Override
    public Code getByIdAndType(String id, CodeType type) {
        return codeRepository.findByIdAndType(id, type);
    }

    @Override
    public int deleteCheckDateAndType(String id, Date date, CodeType type) {
        return codeRepository.deleteByIdCheckDateAndType(id, date, type);
    }
}
