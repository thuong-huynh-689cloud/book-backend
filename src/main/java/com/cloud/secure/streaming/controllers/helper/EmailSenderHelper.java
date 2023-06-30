package com.cloud.secure.streaming.controllers.helper;

import com.cloud.secure.streaming.common.enums.Language;
import com.cloud.secure.streaming.common.enums.UserType;
import com.cloud.secure.streaming.common.notification.AWSEmailSender;
import com.cloud.secure.streaming.common.utilities.ApplicationConfigureValues;
import com.cloud.secure.streaming.controllers.model.request.EmailSender;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.log.NullLogChute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import javax.validation.constraints.NotNull;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Properties;

/**
 * @author 689Cloud
 */
@Slf4j
@Component
public class EmailSenderHelper {
    @Autowired
    private ApplicationConfigureValues appConfig;

    private SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
    private DecimalFormat df = new DecimalFormat("###,###,###");

    public String createTemplate(ModelMap model, String templateName) {
        try {
            Properties properties = new Properties();
            properties.setProperty("input.encoding", "UTF-8");
            properties.setProperty("output.encoding", "UTF-8");
            properties.setProperty("resource.loader", "class");
            properties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            properties.setProperty("runtime.log.logsystem.class", NullLogChute.class.getName());
            VelocityEngine velocityEngine = new VelocityEngine(properties);
            VelocityContext context = new VelocityContext();
            for (Map.Entry<String, Object> entry : model.entrySet()) {
                context.put(entry.getKey(), entry.getValue());
            }
            StringWriter stringWriter = new StringWriter();

            velocityEngine.mergeTemplate("/templates/" + templateName, "UTF-8", context, stringWriter);
            return stringWriter.toString();
        } catch (Exception e) {
            log.error("mail body ", e);
            return null;
        }
    }

    private void mapUrl(ModelMap model) {
        model.put("email_logo", appConfig.LOGO_URL);
        model.put("email_learn_more", appConfig.WEBSITE_LEARN_MORE_URL);
        model.put("email_privacy", appConfig.WEBSITE_PRIVATE_POLICY_URL);
    }

    public void sendEmailResetPassword(@NotNull String email,UserType type, @NotNull String name, String resetCode, Language language) {

        String subject = "Reset Password";

        String urlPrefix;
        String changeEmailLink;
        switch (type) {
            case INSTRUCTOR:
                urlPrefix = appConfig.SEVER_HOST_INSTRUCTOR;
                break;
            case LEARNER:
                urlPrefix = appConfig.SEVER_HOST_LEARNER;
                break;
            default:
                urlPrefix = appConfig.SEVER_HOST;
                break;
        }
        changeEmailLink = urlPrefix + "reset-password/" + resetCode;

        try {

            // Add email notify
            ModelMap model = new ModelMap();
            model.put("subject", subject);
            model.put("user_name", name);
            model.put("email", email);
            model.put("confirm_link", changeEmailLink);
            model.put("resetCode", resetCode);
            // get url for mail template footer
            mapUrl(model);

            String body = createTemplate(model, "en" + "/request-forgot-password.vm");
            System.out.println(body);
            EmailSender emailSender = new EmailSender();
            emailSender.setSubject(subject);
            emailSender.setToEmail(email)
            ;
            emailSender.setBody(body);

            // send mail
            AWSEmailSender.getInstance(appConfig).doSendMail(emailSender);

        } catch (Exception ex) {
            log.error("sendEmailForgotPassword()", ex);
        }
    }

    public void sendChangeEmailRequest(@NotNull String email,UserType type, @NotNull String name, String changeEmailCode, Language language) {

        String subject = "Change Email";

        String urlPrefix;
        String changeEmailLink;
        switch (type) {
            case INSTRUCTOR:
                urlPrefix = appConfig.SEVER_HOST_INSTRUCTOR;
                break;
            case LEARNER:
                urlPrefix = appConfig.SEVER_HOST_LEARNER;
                break;
            default:
                urlPrefix = appConfig.SEVER_HOST;
                break;
        }

        changeEmailLink = urlPrefix + "change-email/" + changeEmailCode;

        try {

            // Add email notify
            ModelMap model = new ModelMap();
            model.put("subject", subject);
            model.put("user_name", name);
            model.put("email", email);
            model.put("confirm_link", changeEmailLink);
            // get url for mail template footer
            mapUrl(model);

            String body = createTemplate(model, "en" + "/change-email.vm");
            System.out.println(body);
            EmailSender emailSender = new EmailSender();
            emailSender.setSubject(subject);
            emailSender.setToEmail(email)
            ;
            emailSender.setBody(body);

            // send mail
            AWSEmailSender.getInstance(appConfig).doSendMail(emailSender);

        } catch (Exception ex) {
            log.error("sendChangeEmailRequest()", ex);
        }
    }

    public void sendEmailSignUpAccount(@NotNull String email, @NotNull String name,
                                         Language language, UserType userType) {

        String link = appConfig.SEVER_HOST_INSTRUCTOR + "sign-in";
        if (userType.equals(UserType.LEARNER)) {
            link = appConfig.SEVER_HOST_LEARNER + "sign-in";
        }
        String subject = "Viboo - Sign up account successfully";

        try {

            // Add email notify
            ModelMap model = new ModelMap();
            model.put("subject", subject);
            model.put("user_name", name);
            model.put("email", email);
            model.put("confirm_link", link);
            // get url for mail template footer
            mapUrl(model);

            String body = createTemplate(model, "en" + "/sign-up.vm");
            System.out.println(body);
            EmailSender emailSender = new EmailSender();
            emailSender.setSubject(subject);
            emailSender.setToEmail(email)
            ;
            emailSender.setBody(body);

            // send mail
            AWSEmailSender.getInstance(appConfig).doSendMail(emailSender);

        } catch (Exception ex) {
            log.error("sendChangeEmailRequest()", ex);
        }
    }

    public void sendEmailCreateUser(@NotNull String email, @NotNull String name, String pwRaw, String pw, Language language, UserType type) {
        String subject = "Create user";

        String urlPrefix;
        String loginLink;
        String forgotPasswordLink;

        switch (type) {
            case INSTRUCTOR:
                urlPrefix = appConfig.SEVER_HOST_INSTRUCTOR;
                break;
            case LEARNER:
                urlPrefix = appConfig.SEVER_HOST_LEARNER;
                break;
            default:
                urlPrefix = appConfig.SEVER_HOST;
                break;
        }
        loginLink = urlPrefix + "sign-in";
        forgotPasswordLink = urlPrefix + "forgot-password";

        try {
            // Add email notify
            ModelMap model = new ModelMap();
            model.put("subject", subject);
            model.put("user_name", name);
            model.put("email", email);
            model.put("auto_password", pwRaw);
            model.put("forgot_password_link", forgotPasswordLink);
            model.put("login_link", loginLink);
            // get url for mail template footer
            mapUrl(model);

            String body;
            if (pw != null && !pw.isEmpty()) {
                body = createTemplate(model, "en" + "/admin-create-account-with-manual-password.vm");
            } else {
                body = createTemplate(model, "en" + "/admin-create-account-with-auto-password.vm");
            }

            EmailSender emailSender = new EmailSender();
            emailSender.setSubject(subject);
            emailSender.setToEmail(email);

            emailSender.setBody(body);

            // send mail
            AWSEmailSender.getInstance(appConfig).doSendMail(emailSender);

        } catch (Exception ex) {
            log.error("sendChangeEmailRequest()", ex);
        }
    }
    public void sendEmailChangePassword(@NotNull String email,UserType type, @NotNull String name, Language language) {

        String subject = "Change Password";

        String urlPrefix;
        String forgotPasswordLink;
        switch (type) {
            case INSTRUCTOR:
                urlPrefix = appConfig.SEVER_HOST_INSTRUCTOR;
                break;
            case LEARNER:
                urlPrefix = appConfig.SEVER_HOST_LEARNER;
                break;
            default:
                urlPrefix = appConfig.SEVER_HOST;
                break;
        }
        forgotPasswordLink = urlPrefix + "forgot-password";
        try {

            // Add email notify
            ModelMap model = new ModelMap();
            model.put("subject", subject);
            model.put("user_name", name);
            model.put("email", email);
            model.put("confirm_link", forgotPasswordLink);
            // get url for mail template footer
            mapUrl(model);

            String body = createTemplate(model, "en" + "/change-password.vm");
            System.out.println(body);
            EmailSender emailSender = new EmailSender();
            emailSender.setSubject(subject);
            emailSender.setToEmail(email)
            ;
            emailSender.setBody(body);

            // send mail
            AWSEmailSender.getInstance(appConfig).doSendMail(emailSender);

        } catch (Exception ex) {
            log.error("sendEmailChangePassword()", ex);
        }
    }

    public void sendEmailAdminChangePasswordUser(String name,
                                                 String email,
                                                 UserType type,
                                                 String passwordRandom,
                                                 Language language) {
        String subject = "Reset Password";

        String urlPrefix;
        switch (type) {
            case INSTRUCTOR:
                urlPrefix = appConfig.SEVER_HOST_INSTRUCTOR;
                break;
            case LEARNER:
                urlPrefix = appConfig.SEVER_HOST_LEARNER;
                break;
            default:
                urlPrefix = appConfig.SEVER_HOST;
                break;
        }
        urlPrefix = urlPrefix + "/sign-in";
        try {
            // Add email notify
            ModelMap model = new ModelMap();
            model.put("subject", subject);
            model.put("user_name", name);
            model.put("email", email);
            model.put("password", passwordRandom);
            model.put("login_link", urlPrefix);
            // get url for mail template footer
            mapUrl(model);

            String body = createTemplate(model, "en" + "/admin-change-password-user.vm");
            System.out.println(body);
            EmailSender emailSender = new EmailSender();
            emailSender.setSubject(subject);
            emailSender.setToEmail(email)
            ;
            emailSender.setBody(body);

            // send mail
            AWSEmailSender.getInstance(appConfig).doSendMail(emailSender);

        } catch (Exception ex) {
            log.error("sendEmailChangePassword()", ex);
        }
    }
}