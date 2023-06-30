package com.cloud.secure.streaming.common.notification;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.RawMessage;
import com.amazonaws.services.simpleemail.model.SendRawEmailRequest;
import com.amazonaws.services.simpleemail.model.SendRawEmailResult;
import lombok.extern.slf4j.Slf4j;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.Callable;

@Slf4j
public class AWSCallableTaskSendRequestRaw implements Callable<String> {
    private final String TO;
    private final String SUBJECT;
    private final String BODY;
    private final AmazonSimpleEmailServiceClient client;
    private final String FROM;
    private final String pathAttachment;

    @Override
    public String call() throws Exception {
        System.out.println("start");
        long startTime = new Date().getTime();
        Session session = Session.getDefaultInstance(new Properties());

        // Create a new MimeMessage object.
        MimeMessage message = new MimeMessage(session);

        // Add subject, from and to lines.
        message.setSubject(SUBJECT, "UTF-8");
        message.setContent(BODY, "text/html; charset=UTF-8");

        message.setFrom(new InternetAddress(FROM));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(TO));

        // Create a multipart/alternative child container.
        MimeMultipart msg_body = new MimeMultipart("alternative");

        // Create a wrapper for the HTML and text parts.
        MimeBodyPart wrap = new MimeBodyPart();

        // Define the text part.
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setContent(BODY, "text/plain; charset=UTF-8");

        // Define the HTML part.
        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(BODY, "text/html; charset=UTF-8");

        // Add the text and HTML parts to the child container.
        msg_body.addBodyPart(textPart);
        msg_body.addBodyPart(htmlPart);

        // Add the child container to the wrapper object.
        wrap.setContent(msg_body);

        // Create a multipart/mixed parent container.
        MimeMultipart msg = new MimeMultipart("mixed");

        // Add the parent container to the message.
        message.setContent(msg);

        // Add the multipart/alternative part to the message.
        msg.addBodyPart(wrap);

        // Define the attachment
        if (pathAttachment != null && !"".equals(pathAttachment)) {
            MimeBodyPart att = new MimeBodyPart();

            DataSource fds = new FileDataSource(pathAttachment);

            // check file exists
            File file = new File(pathAttachment);
            log.info("Path attachment: " + pathAttachment + " : " + file.exists());

            att.setDataHandler(new DataHandler(fds));
            att.setFileName(fds.getName());

            // Add the attachment to the message.
            msg.addBodyPart(att);
        }

        // Try to send the email.
        try {
            System.out.println("Attempting to send an email through Amazon SES using the AWS SDK for Java...");

//            // Print the raw email content on the console
//            PrintStream out = System.out;
//            message.writeTo(out);

            // Send the email.
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            message.writeTo(outputStream);
            RawMessage rawMessage = new RawMessage(ByteBuffer.wrap(outputStream.toByteArray()));

            SendRawEmailRequest rawEmailRequest = new SendRawEmailRequest(rawMessage);

            SendRawEmailResult sendEmailResult = client.sendRawEmail(rawEmailRequest);
            if (sendEmailResult != null) {
                long timeSpend = (new Date().getTime() - startTime);
                log.info("Amazon SES has sent email to " + TO + " | time spent=" + timeSpend + "miliseconds | messageId=" + sendEmailResult.getMessageId());
            }
            System.out.println("Email sent!");

        } catch (Exception ex) { // Display an error if something goes wrong.
            System.out.println("Email Failed");
            System.err.println("Error message: " + ex.getMessage());
            ex.printStackTrace();
        }
        return "OK";
    }

    public AWSCallableTaskSendRequestRaw(AmazonSimpleEmailServiceClient client, String to, String subject, String body,
                                      String from, String pathAttachment) {
        this.TO = to;
        this.SUBJECT = subject;
        this.BODY = body;
        this.client = client;
        this.FROM = from;
        this.pathAttachment = pathAttachment;
    }
}
