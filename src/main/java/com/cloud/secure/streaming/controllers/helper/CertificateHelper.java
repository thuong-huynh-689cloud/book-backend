package com.cloud.secure.streaming.controllers.helper;

import com.cloud.secure.streaming.common.utilities.DateUtil;
import com.cloud.secure.streaming.common.utilities.UniqueID;
import com.cloud.secure.streaming.controllers.model.request.CreateCertificateRequest;
import com.cloud.secure.streaming.controllers.model.request.UpdateCertificateRequest;
import com.cloud.secure.streaming.entities.Certificate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author 689Cloud
 */
@Component
public class CertificateHelper {

    public Certificate createCertificate(String userId, CreateCertificateRequest createCertificateRequest) {

        Certificate certificate = new Certificate();
        // add Id to data
        certificate.setId(UniqueID.getUUID());
        // add file name to data
        certificate.setName(createCertificateRequest.getName());
        // add path to data
        certificate.setPath(createCertificateRequest.getPath());
        // add userId to data
        certificate.setUserId(userId);
        // set Created Date
        certificate.setCreatedDate(DateUtil.convertToUTC(new Date()));

        return certificate;
    }


    public Certificate updateCertificate(Certificate certificate, UpdateCertificateRequest updateCertificateRequest) {
        // Update name
        if (updateCertificateRequest.getName() != null && !updateCertificateRequest.getName().trim().isEmpty() &&
                !updateCertificateRequest.getName().trim().equals(certificate.getName())) {
            certificate.setName(updateCertificateRequest.getName().trim());
        }
        // Update Title
        if (updateCertificateRequest.getPath() != null && !updateCertificateRequest.getPath().trim().isEmpty() &&
                !updateCertificateRequest.getPath().trim().equals(certificate.getPath())) {
            certificate.setPath(updateCertificateRequest.getPath().trim());
        }

        return certificate;
    }
}

