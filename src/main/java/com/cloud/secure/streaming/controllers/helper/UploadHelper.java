package com.cloud.secure.streaming.controllers.helper;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.cloud.secure.streaming.amazon.s3.AmazonS3Util;
import com.cloud.secure.streaming.common.enums.APIStatusMessage;
import com.cloud.secure.streaming.common.exceptions.ApplicationException;
import com.cloud.secure.streaming.common.utilities.*;
import com.cloud.secure.streaming.controllers.model.tmp.Image;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author 689Cloud
 */
@Component
@Slf4j
public class UploadHelper {

//    public String uploadFile(String filePathDirectory, MultipartFile file, HttpServletRequest request,
//                             CannedAccessControlList cannedAccessControlList, AmazonS3Util amazonS3Util) {
//        String fileName = null;
//        try {
//            if (file != null && !file.isEmpty()) {
//
//                StringBuilder filePathBuilder = new StringBuilder();
//
//                Validator.notNull(filePathDirectory, RestAPIStatus.INTERNAL_SERVER_ERROR,APIStatusMessage.INTERNAL_SERVER_ERROR);
//
//                // generate uuid file name //upload/name.ext
//                String ext = AppUtil.getFileExtension(file);
//                String uniqueID = UUID.randomUUID().toString();
//                filePathBuilder
//                        .append(filePathDirectory)
//                        .append("/")
//                        .append(uniqueID)
//                        .append(".")
//                        .append(ext);
//
//                String filePath = request.getServletContext().getRealPath("/") + filePathBuilder.toString();
//                File f = new File(filePath);
//
//                // create parent directories if they don't exist
//                if (!f.getParentFile().exists()) {
//                    f.getParentFile().mkdirs();
//                }
//
//                // write multipart file to disk
//                FileUtils.writeByteArrayToFile(f, file.getBytes());
//
//                boolean isUploaded = amazonS3Util.uploadFile(filePath, filePathBuilder.toString(), cannedAccessControlList);
//                if (isUploaded) {
//                    fileName = f.getName();
//
//                    if (f.exists()) {
//                        f.delete();
//                    }
//                }
//            }
//            return fileName;
//        } catch (IOException ex) {
//            throw new ApplicationException(RestAPIStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    public boolean downloadFile(String downloadFilePath, String s3FilePath, String s3FileName, AmazonS3Util amazonS3Util) {
//
//
//        StringBuilder filePathBuilder = new StringBuilder();
//
//
//        filePathBuilder
//                .append(s3FilePath)
//                .append("/")
//                .append(s3FileName);
//        File f = new File(downloadFilePath);
//
//        // create parent directories if they don't exist
//        if (!f.getParentFile().exists()) {
//            f.getParentFile().mkdirs();
//        }
//
//        return amazonS3Util.downLoadToLocaServer(downloadFilePath, filePathBuilder.toString(), s3FileName);
//    }
//

//    public Image addImage(MultipartFile images, HttpServletRequest request, String path, AmazonS3Util amazonS3Util) {
//        Image image = null;
//        if (images != null) {
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(DateUtil.convertToUTC(new Date()));
//
//            image = new Image();
//            String imageName = uploadFile(path, images, request, null, amazonS3Util);
//            image.setImageName(imageName);
//            image.setId(UniqueID.getUUID());
//            calendar.add(Calendar.SECOND, 1);
//            image.setDateSort(calendar.getTime());
//        }
//        return image;
//    }
//
//    public void checkOneFileExtension(MultipartFile file) {
//        String extensionType = FilenameUtils.getExtension(file.getOriginalFilename()).toLowerCase();
//        String[] extensions = {"png", "pjp", "jpg", "jpeg", "jfif", "pjpeg", "ico"};
//        if (!Arrays.asList(extensions).contains(extensionType)) {
//            throw new ApplicationException(RestAPIStatus.ERR_INVALID_FILE);
//        }
//    }
//
//    public void checkCourseImageExtension(MultipartFile file) {
//        String extensionType = FilenameUtils.getExtension(file.getOriginalFilename()).toLowerCase();
//        checkCourseImageSize
//        if (!Arrays.asList(extensions).contains(extensionType)) {
//            throw new ApplicationException(RestAPIStatus.ERR_INVALID_FILE);
//        }
//    }
//
//    public void checkPromotionVideoExtension(MultipartFile file) {
//        String extensionType = FilenameUtils.getExtension(file.getOriginalFilename()).toLowerCase();
//        String[] extensions = {"mp4", "mov", "m4a", "3gp", "3g2", "mj2"};
//        if (!Arrays.asList(extensions).contains(extensionType)) {
//            throw new ApplicationException(RestAPIStatus.ERR_INVALID_FILE);
//        }
//    }
//
//    public void checkFileExtensionAndSizeMultiple(MultipartFile[] files) {
//        Arrays.stream(files).forEach(file -> {
//            // get extension
//            String extensionType = FilenameUtils.getExtension(file.getOriginalFilename()).toLowerCase();
//            String[] extensions = {"png", "pjp", "jpg", "jpeg", "jfif", "pjpeg", "ico"};
//            if (!Arrays.asList(extensions).contains(extensionType)) {
//                throw new ApplicationException(RestAPIStatus.ERR_INVALID_FILE);
//            }
//            // get size
//            Long sizeInBytes = file.getSize();
//            long sizeInMb = sizeInBytes / (1024 * 1024);
//            System.out.println(sizeInMb);
//            if (sizeInMb > 2) {
//                throw new ApplicationException(RestAPIStatus.ERR_INVALID_FILE);
//            }
//        });
//    }
//
//    public void checkResourceFileSize(MultipartFile file) {
//        Long sizeInBytes = file.getSize();
//        long sizeInMb = sizeInBytes / 1000 / 1000;
//        if (sizeInMb > 1024) {
//            throw new ApplicationException(RestAPIStatus.ERR_TOO_BIG_RESOURCE_FILE);
//        }
//    }
//
//    public void checkLectureFileSize(MultipartFile multipartFile) {
//        Long sizeInBytes = multipartFile.getSize();
//        long sizeInMb = sizeInBytes / 1000 / 1000;
//        if (sizeInMb > 4096) {
//            throw new ApplicationException(RestAPIStatus.ERR_TOO_BIG_LECTURE_FILE);
//        }
//    }
//
//    public void checkCertificateFileSize(MultipartFile file) {
//        Long sizeInBytes = file.getSize();
//        long sizeInMb = sizeInBytes / 1000 / 1000;
//        if (sizeInMb > 12) {
//            throw new ApplicationException(RestAPIStatus.ERR_TOO_BIG_CERTIFICATE_FILE);
//        }
//    }
//
//    public void checkCourseImageSize(MultipartFile file) {
//        Long sizeInBytes = file.getSize();
//        long sizeInMb = sizeInBytes / 1000 / 1000;
//        if (sizeInMb > 10) {
//            throw new ApplicationException(RestAPIStatus.ERR_TOO_BIG_COURSE_IMAGE_FILE);
//        }
//    }
//
//    public void checkPromotionVideoFileSize(MultipartFile file) {
//        Long sizeInBytes = file.getSize();
//        long sizeInMb = sizeInBytes / 1000 / 1000;
//        if (sizeInMb > 1024) {
//            throw new ApplicationException(RestAPIStatus.ERR_TOO_BIG_RESOURCE_FILE);
//        }
//    }

    public void checkPromotionVideoFileSize(long size) {
        long sizeInMb = size / 1000 / 1000;
        if (sizeInMb > 1024) {
            throw new ApplicationException(RestAPIStatus.ERR_TOO_BIG_RESOURCE_FILE);
        }
    }

    public String uploadFile(MultipartFile file, HttpServletRequest request,
                             CannedAccessControlList cannedAccessControlList, ApplicationConfigureValues applicationConfigureValues,
                             AmazonS3Util amazonS3Util) {
        String filePathDirectory = applicationConfigureValues.uploadPath;
        String fileName = null;
        try {
            if (file != null && !file.isEmpty()) {

                StringBuilder filePathBuilder = new StringBuilder();

                Validator.notNull(filePathDirectory, RestAPIStatus.INTERNAL_SERVER_ERROR, APIStatusMessage.INTERNAL_SERVER_ERROR);

                // generate uuid file name //upload/name.ext
                String ext = AppUtil.getFileExtension(file);
                String uniqueID = UUID.randomUUID().toString();
                filePathBuilder
                        .append(filePathDirectory)
                        .append("/")
                        .append(uniqueID)
                        .append(".")
                        .append(ext);

                String filePath = request.getServletContext().getRealPath("/") + filePathBuilder.toString();
                File f = new File(filePath);

                // create parent directories if they don't exist
                if (!f.getParentFile().exists()) {
                    f.getParentFile().mkdirs();
                }

                // write multipart file to disk
                FileUtils.writeByteArrayToFile(f, file.getBytes());

                boolean isUploaded = amazonS3Util.uploadFile(filePath, filePathBuilder.toString(), cannedAccessControlList);
                if (isUploaded) {
                    fileName = filePathBuilder.toString();

                    if (f.exists()) {
                        f.delete();
                    }
                }
            }
            return fileName;
        } catch (IOException ex) {
            throw new ApplicationException(RestAPIStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
