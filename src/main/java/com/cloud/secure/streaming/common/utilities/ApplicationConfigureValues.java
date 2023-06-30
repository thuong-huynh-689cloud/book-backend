package com.cloud.secure.streaming.common.utilities;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author 689Cloud
 */
@Component
public class ApplicationConfigureValues {

    @Value("${aws.ses.smtp.username}")
    public String AWS_SES_SMTP_USERNAME;

    @Value("${aws.ses.smtp.password}")
    public String AWS_SES_SMTP_PASSWORD;

    @Value("${aws.ses.smtp.host}")
    public String AWS_SES_SMTP_HOST;

    @Value("${aws.ses.smtp.port}")
    public String AWS_SES_SMTP_PORT;

    @Value("${aws.ses.smtp.from}")
    public String AWS_SES_SMTP_FROM;

    @Value("${aws.ses.smtp.sender.name}")
    public String AWS_SES_SMTP_SENDER_NAME;

    @Value("${admin-ui-url}")
    public String SEVER_HOST;

    @Value("${instructor-ui-url}")
    public String SEVER_HOST_INSTRUCTOR;

    @Value("${learner-ui-url}")
    public String SEVER_HOST_LEARNER;

    @Value("${logo-url}")
    public String LOGO_URL;

    @Value("${learn-more-url}")
    public String WEBSITE_LEARN_MORE_URL;

    @Value("${private-policy-url}")
    public String WEBSITE_PRIVATE_POLICY_URL;

    @Value("${ses-access-key}")
    public String AWS_ACCESS_KEY;

    @Value("${ses-secret-key}")
    public String AWS_SES_SECRET_KEY;

    @Value("${ses-email}")
    public String AWS_SES_EMAIL;

    @Value("${ses-region}")
    public String AWS_SES_REGION;

    @Value("${app.amazons3.accesskey}")
    public String awsS3AccessKey;

    @Value("${app.amazons3.secretkey}")
    public String awsS3SecretKey;

    @Value("${app.amazons3.bucket}")
    public String awsS3Bucket;

    @Value("${app.amazons3.region}")
    public String awsS3Region;

    @Value("${path.user}")
    public String userPath;

    @Value("${path.course}")
    public String coursePath;

    @Value("${path.upload}")
    public String uploadPath;

    @Value("${google.api.client-id}")
    public String GOOGLE_CLIENT_ID;

    @Value("${facebook.api.client-id}")
    public String FACEBOOK_CLIENT_ID;

    @Value("${ffprobe.path}")
    public String ffprobePath;

    @Value("${ffmpeg.path}")
    public String ffmpegPath;

    @Value("${upload-video.path}")
    public String uploadVideoPath;

    @Value("${encrypt-video.path}")
    public String encryptVideoPath;

    @Value("${streaming.apiPrefix}")
    public String streamingApiPrefix;

    @Value("${path.video-input}")
    public String pathVideoInput;

    @Value("${path.encrypted}")
    public String encrypted;

    @Value("${path.non-encrypted}")
    public String nonEncrypted;

    @Value("${encoded.key}")
    public String encodedKey;
}

