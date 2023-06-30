package com.cloud.secure.streaming.amazon.s3;

import com.amazonaws.AmazonClientException;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.transfer.MultipleFileUpload;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.cloud.secure.streaming.common.utilities.ApplicationConfigureValues;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class AmazonS3Util {

    private final ApplicationConfigureValues appConfig;

    //AmazonS3Client
    private AmazonS3 s3Client;
    String bucketName = "";

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    //Init AmazonS3Client
    public AmazonS3Util(ApplicationConfigureValues appConfig) {
        this.appConfig = appConfig;
    }

    private void AmazonS3Setup() {
        if (s3Client == null) {
            AWSCredentials credentials;
            try {
                // Load config
                String accessKey = appConfig.awsS3AccessKey;
                String secretKey = appConfig.awsS3SecretKey;
                bucketName = appConfig.awsS3Bucket;
                String regionId = appConfig.awsS3Region;
                credentials = new BasicAWSCredentials(accessKey, secretKey);
                s3Client = new AmazonS3Client(credentials);
                Region region = Region.fromValue(regionId);
                s3Client.setRegion(region.toAWSRegion());
            } catch (Exception e) {
                e.printStackTrace();
                // "Cannot load the credentials from the credential profiles file. ",e);
            }
        }
    }


    /**
     * downLoadToLocaServer
     *
     * @param targetPath
     * @param keyName    => the file name
     */
    public boolean downLoadToLocaServer(String targetPath, String keyName, String fileName) {
        try {
            AmazonS3Setup();
            log.info("Process download from Amazon to local server key name=" + keyName);
            System.out.println("Downloading an object");
            S3Object object = s3Client.getObject(new GetObjectRequest(bucketName, keyName));
            System.out.println("Content-Type: " + object.getObjectMetadata().getContentType());
            object.getObjectContent();
            String targetFilePath = targetPath + fileName;
            File targetFile = new File(targetFilePath);
            FileUtils.copyInputStreamToFile(object.getObjectContent(), targetFile);

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * getURLDownload
     *
     * @param keyName
     * @return
     */
    public String getURLDownload(String keyName, long expiryDuration) {
        String urlString = "";
        try {
            AmazonS3Setup();
            System.out.println("Generating pre-signed URL.");
            java.util.Date expiration = new java.util.Date();
            long milliSeconds = expiration.getTime();
            milliSeconds += expiryDuration; // Add 3 hours.
            expiration.setTime(milliSeconds);
            GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, keyName);
            generatePresignedUrlRequest.setMethod(HttpMethod.GET);
            generatePresignedUrlRequest.setExpiration(expiration);
            URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
            urlString = url.toString();
            log.info("Generating pre-signed URL Amazon=" + urlString);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return urlString;
    }


    /**
     * uploadFile
     *
     * @param filePath
     * @param keyName: keyName=teamId + "/" + uploadFile.getFilePath();
     */
    // Set part size to 5 MB.
    // Fixed issue https://forums.aws.amazon.com/thread.jspa?threadID=91771
    long partSize = 5 * 1024 * 1024;

    public boolean uploadFile(String filePath, String keyName, CannedAccessControlList cannedAccessControlList) {
        try {
            System.out.println("Process upload to Amazon key name=" + keyName);
            log.info("Process upload to Amazon key name=" + keyName);
            AmazonS3Setup();
            // Create a list of UploadPartResponse objects. You get one of these
            // for each part upload.
            List<PartETag> partETags = new ArrayList<>();
            // Step 1: Initialize.
            InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(bucketName, keyName);
            // Set public access
            if(cannedAccessControlList!=null)
                initRequest.setCannedACL(cannedAccessControlList);

            InitiateMultipartUploadResult initResponse = s3Client.initiateMultipartUpload(initRequest);

            File file = new File(filePath);
            long contentLength = file.length();

            try {
                // Step 2: Upload parts.
                long filePosition = 0;
                for (int i = 1; filePosition < contentLength; i++) {
                    // Last part can be less than 5 MB. Adjust part size.
                    long _partSize = Math.min(partSize, (contentLength - filePosition));

                    // Create request to upload a part.
                    UploadPartRequest uploadRequest = new UploadPartRequest()
                            .withBucketName(bucketName)
                            .withKey(keyName)
                            .withUploadId(initResponse.getUploadId())
                            .withPartNumber(i)
                            .withFileOffset(filePosition)
                            .withFile(file)
                            .withPartSize(_partSize);
                    // Upload part and add response to our list.
                    partETags.add(s3Client.uploadPart(uploadRequest).getPartETag());
                    filePosition += _partSize;
                }

                // Step 3: Complete.
                CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(
                        bucketName,
                        keyName,
                        initResponse.getUploadId(),
                        partETags);

                s3Client.completeMultipartUpload(compRequest);

                return true;
            } catch (Exception e) {
                System.err.println("Error when upload file in to S3: " + e.getMessage());
                s3Client.abortMultipartUpload(new AbortMultipartUploadRequest(
                        bucketName, keyName, initResponse.getUploadId()));
                log.error(e.getMessage());
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return false;
        }

    }

    /**
     * @param keyName: keyName=teamId + "/" + uploadFile.getFilePath();
     * @return
     */
    public boolean deleteFile(String keyName) {

        try {
            AmazonS3Setup();
            s3Client.deleteObject(new DeleteObjectRequest(bucketName, keyName));
            log.info("Process delete file from Amazon key name=" + keyName);
            return true;
        } catch (AmazonClientException ace) {
            return false;
        }
    }

    public boolean deleteFolder(String pathFolder) {
        try {
            AmazonS3Setup();
            ObjectListing objectList = this.s3Client.listObjects(bucketName, pathFolder);
            List<S3ObjectSummary> objectSummeryList = objectList.getObjectSummaries();
            String[] keysList = new String[objectSummeryList.size()];
            int count = 0;
            for (S3ObjectSummary summery : objectSummeryList) {
                keysList[count++] = summery.getKey();
            }
            DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketName).withKeys(keysList);
            this.s3Client.deleteObjects(deleteObjectsRequest);
            return true;
        } catch (AmazonClientException ace) {
            return false;
        }
    }


    /**
     * @param keyNames
     * @return
     */
    public boolean deleteFiles(List<String> keyNames) {
        AmazonS3Setup();
        DeleteObjectsRequest multiObjectDeleteRequest = new DeleteObjectsRequest(bucketName);
        List<DeleteObjectsRequest.KeyVersion> keys = new ArrayList<DeleteObjectsRequest.KeyVersion>();
        for (String keyItem : keyNames) {
            keys.add(new DeleteObjectsRequest.KeyVersion(keyItem));
        }
        multiObjectDeleteRequest.setKeys(keys);

        try {
            DeleteObjectsResult delObjRes = s3Client.deleteObjects(multiObjectDeleteRequest);
            System.out.format("Successfully deleted all the %s items.\n", delObjRes.getDeletedObjects().size());

        } catch (MultiObjectDeleteException e) {
            // Process exception.
            return false;
        }
        return true;
    }

    public boolean uploadFolder(String pathFolder, String keyName, boolean isPublic) {
        String uploadPath = appConfig.uploadPath + "/" + keyName;
        try {
            AmazonS3Setup();
            TransferManager xfer_mgr = TransferManagerBuilder.standard().withS3Client(s3Client).build();
            MultipleFileUpload xfer = xfer_mgr.uploadDirectory(bucketName, uploadPath, new File(pathFolder), true);
            XferMgrProgress.showTransferProgress(xfer);
            XferMgrProgress.waitForCompletion(xfer);
            if (isPublic) {
                // We only want the keys that are in the folder
                ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                        .withBucketName(bucketName)
                        .withPrefix(appConfig.uploadPath + "/" + keyName);
                ObjectListing objectListing;
                // Iterate over all the matching keys
                do {
                    objectListing = s3Client.listObjects(listObjectsRequest);
                    for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                        // Apply the ACL
                        s3Client.setObjectAcl(bucketName, objectSummary.getKey(), CannedAccessControlList.PublicRead);
                    }
                    listObjectsRequest.setMarker(objectListing.getNextMarker());
                } while (objectListing.isTruncated());
            } else {
                String thumbNailPath = uploadPath + "/thumbnail.png";
                s3Client.setObjectAcl(bucketName, thumbNailPath, CannedAccessControlList.PublicRead);
            }
            return true;
        } catch (AmazonClientException ace) {
            System.out.println(ace);
            return false;
        }
    }
}
