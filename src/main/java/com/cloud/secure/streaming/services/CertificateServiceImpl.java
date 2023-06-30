package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.entities.Certificate;
import com.cloud.secure.streaming.repositories.CertificateRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 689Cloud
 */
@Service
public class CertificateServiceImpl implements CertificateService {

    final CertificateRepository certificateRepository;

    public CertificateServiceImpl(CertificateRepository certificateRepository) {
        this.certificateRepository = certificateRepository;
    }

    @Override
    public Certificate save(Certificate certificate) {
        return certificateRepository.save(certificate);
    }

    @Override
    public void delete(Certificate certificate) {
        certificateRepository.delete(certificate);
    }

    @Override
    public Certificate getById(String id) {
        return certificateRepository.findById(id).orElse(null);
    }

    @Override
    public List<Certificate> getAllByUserIdOrderByCreatedDateDesc(String userId) {
        return certificateRepository.findAllByUserIdOrderByCreatedDateDesc(userId);
    }

    @Override
    public void deleteByIdIn(List<String> certificateIds) {
        certificateRepository.deleteByIdIn(certificateIds);
    }

    @Override
    public void deleteByUserId(List<String> certificateIds, String userId) {
        certificateRepository.deleteByUserId(certificateIds, userId);
    }

    @Override
    public void saveAll(List<Certificate> certificate) {
        certificateRepository.saveAll(certificate);
    }


}
