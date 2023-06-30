package com.cloud.secure.streaming.common.notification;

import com.cloud.secure.streaming.common.utilities.ApplicationConfigureValues;
import lombok.extern.slf4j.Slf4j;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;


/**
 * @author 689Cloud
 */
@Slf4j
public class AWSCallableTaskSendRequest implements Callable<String> {
    static final int PORT = 25;
    private String[] TO;
    private String SUBJECT;
    private String BODY;
    private String senderName;
    private String attachment;
    private ApplicationConfigureValues applicationValueConfigure;

    public AWSCallableTaskSendRequest(ApplicationConfigureValues applicationValueConfigure, String[] to, String subject, String body, String attachment, String senderName) {
        this.applicationValueConfigure = applicationValueConfigure;
        this.TO = to;
        this.SUBJECT = subject;
        this.BODY = body;
        this.attachment = attachment;
        this.senderName = senderName;
    }

    @Override
    public String call() throws Exception {
        // Create a Properties object to contain connection configuration information.
        Properties props = System.getProperties();
        props.put("mail.transport.protocol", "smtps");
        props.put("mail.smtp.port", PORT);

        // Create a Session object to represent a mail session with the specified properties.
        Session session = Session.getDefaultInstance(props);

        // Create a message with the specified information.
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(applicationValueConfigure.AWS_SES_SMTP_FROM, senderName, "UTF-8"));

        //send TO
        List<InternetAddress> list = new ArrayList<>();
        for (String s : TO) {
            list.add(new InternetAddress(s));
        }
        InternetAddress[] address = list.toArray(new InternetAddress[0]);
        msg.setRecipients(Message.RecipientType.TO, address);

        msg.setSubject(SUBJECT, "UTF-8");
        msg.setContent(BODY, "text/html; charset=UTF-8");

        // creates message part
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(BODY, "text/html; charset=UTF-8");

        // creates multi-part
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        // adds attachments
        if (attachment != null && !"".equals(attachment)) {
            MimeBodyPart attachPart = new MimeBodyPart();
            try {
                attachPart.attachFile(attachment);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            multipart.addBodyPart(attachPart);
        }

        // sets the multi-part as e-mail's content
        msg.setContent(multipart);
        // Create a transport.

        // Send the message.
        try (Transport transport = session.getTransport()) {
            // Connect to Amazon SES using the SMTP username and password you specified above.
            transport.connect(applicationValueConfigure.AWS_SES_SMTP_HOST, applicationValueConfigure.AWS_SES_SMTP_USERNAME, applicationValueConfigure.AWS_SES_SMTP_PASSWORD);

            // Send the email.
            transport.sendMessage(msg, msg.getAllRecipients());
            log.info("Email had been sent!");
            return "OK";
        } catch (Exception ex) {
            log.error("Error message: " + ex.getMessage() + " Cannot send to email " + Arrays.toString(TO));
            log.error("Detail", ex);
            return Arrays.toString(TO);
        }
    }
}
