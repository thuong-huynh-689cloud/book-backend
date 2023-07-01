package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.common.enums.ResetCodeType;
import com.cloud.secure.streaming.entities.ResetCode;
import org.springframework.stereotype.Service;

@Service
public class ResetCodeServiceImpl implements  ResetCodeService{
    final ResetCodeRepository resetCodeRepository;

    public ResetCodeServiceImpl(ResetCodeRepository resetCodeRepository) {
        this.resetCodeRepository = resetCodeRepository;
    }

    @Override
    public ResetCode getByIdAndType(String verifyKey, ResetCodeType type) {
        return resetCodeRepository.findByIdAndType(verifyKey,type);
    }

    @Override
    public ResetCode saveResetCode(ResetCode resetCode) {
        return resetCodeRepository.save(resetCode);
    }

    @Override
    public void deleteResetCode(ResetCode resetCode) {
        resetCodeRepository.delete(resetCode);
    }
}
