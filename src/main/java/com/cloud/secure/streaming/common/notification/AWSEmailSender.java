package com.cloud.secure.streaming.common.notification;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.cloud.secure.streaming.common.utilities.ApplicationConfigureValues;
import com.cloud.secure.streaming.controllers.model.request.EmailSender;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author 689Cloud
 */
@Slf4j
public class AWSEmailSender {

    private AmazonSimpleEmailServiceClient sesClient;
    private String accessKey;
    private String secretKey;
    private String FROM_EMAIL;
    private static AWSEmailSender instance;

    public static synchronized AWSEmailSender getInstance(ApplicationConfigureValues appConfig) {
        if (instance == null) {
            instance = new AWSEmailSender(appConfig);
        }
        return instance;
    }

    private AWSEmailSender(ApplicationConfigureValues appConfig) {
        this.getAwsSESClient(appConfig);
    }

    public AmazonSimpleEmailServiceClient getAwsSESClient(ApplicationConfigureValues appConfig) {

        // Load configuration
        this.accessKey = appConfig.AWS_ACCESS_KEY;
        this.secretKey = appConfig.AWS_SES_SECRET_KEY;
        this.FROM_EMAIL = appConfig.AWS_SES_EMAIL;

        if (sesClient == null) {
            AWSCredentials credentials = null;
            try {
                System.out.println(accessKey + ", " + secretKey);
                credentials = new BasicAWSCredentials(accessKey, secretKey);
            } catch (Exception e) {
                log.error("AmazonSimpleEmailServiceClient auth error", e);
            }
            // Instantiate an Amazon SES client, which will make the service call with the supplied AWS credentials.
            sesClient = new AmazonSimpleEmailServiceClient(credentials);
            // Choose the AWS region of the Amazon SES endpoint you want to connect to. Note that your production
            // access status, sending limits, and Amazon SES identity-related settings are specific to a given
            // AWS region, so be sure to select an AWS region in which you set up Amazon SES. Here, we are using
            // the US East (N. Virginia) region. Examples of other regions that Amazon SES supports are US_WEST_2
            // and EU_WEST_1. For a complete list, see http://docs.aws.amazon.com/ses/latest/DeveloperGuide/regions.html
            Region REGION = Region.getRegion(Regions.fromName(appConfig.AWS_SES_REGION));
            sesClient.setRegion(REGION);
        }

        return sesClient;
    }



    /**
     * Send Email notification by AWS SES Service
     * @param appConfig
     * @param toEmail
     * @param subject
     * @param body
     * @param attachment
     * @param senderName
     */
    public void doSendMail(ApplicationConfigureValues appConfig, String toEmail[], String subject, String body, String attachment, String senderName) {
        try {
            ExecutorService executor = Executors.newFixedThreadPool(5);
            List<Future<String>> listTask = new ArrayList<Future<String>>();
            Callable worker = new AWSCallableTaskSendRequest(appConfig, toEmail,subject, body, attachment, senderName);
            Future<String> submit = executor.submit(worker);
            listTask.add(submit);
            executor.shutdown();

        } catch (Exception e) {
            log.error("doSendMail error", e);
        }
    }

    public void doSendMail(EmailSender emailSender){
        try {

            List<Future<String>> listTask = new ArrayList<Future<String>>();
            List<String> toEmails = Arrays.asList(emailSender.getToEmail().split(","));
            int poolSize = toEmails.size() + 1;
            ExecutorService executor = Executors.newFixedThreadPool(poolSize);
            for (String email: toEmails) {
                String subject = emailSender.getSubject();
                String body = emailSender.getBody();
                String pathAttachment = emailSender.getPathAttachment();

                Callable worker = new AWSCallableTaskSendRequestRaw(sesClient, email, subject, body, this.FROM_EMAIL, pathAttachment);
                Future<String> submit = executor.submit(worker);
                listTask.add(submit);
            }



            List<String> listFileResult = new ArrayList<String>();
            List<String> emailsSendFail = new ArrayList<String>();
            // Now retrieve the result
            for (Future<String> future : listTask) {
                try {
                    String result = future.get();
                    if ("OK".equals(result)) {
                        listFileResult.add(result);
                    } else {
                        emailsSendFail.add(result);
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            // shutdown thread poll
            executor.shutdown();


            log.info("listFileResult: " + listFileResult.size());
            log.info("emailsSendFail: " + emailsSendFail.size());

        } catch (Exception e) {
            log.error("doSendMail error", e);
        }
    }
}
