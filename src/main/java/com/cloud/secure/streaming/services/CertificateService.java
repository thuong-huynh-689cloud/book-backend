package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.entities.Certificate;

import java.util.List;

/**
 * @author 689Cloud
 */
public interface CertificateService {

    Certificate save(Certificate certificate);

    void delete(Certificate certificate);

    Certificate getById(String id);

    List<Certificate> getAllByUserIdOrderByCreatedDateDesc (String userId);

    void deleteByIdIn(List<String> certificateIds);

    void deleteByUserId(List<String> certificateIds,String userId);

    void saveAll( List<Certificate> certificate);
}
