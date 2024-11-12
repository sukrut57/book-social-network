package com.api.socialbookbackend.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    /**
     * Send email verification
     * @param to
     * @param username
     * @param emailTemplateName
     * @param conformationUrl
     * @param activationCode
     * @param subject
     * @throws MessagingException
     */
    @Async
    public void sendEmailVerification(
            String to,
            String username,
            EmailTemplateName emailTemplateName,
            String conformationUrl,
            String activationCode,
            String subject) throws MessagingException {

        //send email verification

        String templateName;
        if(emailTemplateName ==null){
            templateName = "confirm-email";
        }
        else {
            templateName = emailTemplateName.name();
        }

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage,
                MULTIPART_MODE_MIXED,
                UTF_8.name());
        Map<String,Object> properties = new HashMap<>();
        properties.put("username",username);
        properties.put("confirmationUrl",conformationUrl);
        properties.put("activationCode",activationCode);

        Context context = new Context();
        context.setVariables(properties);

        helper.setFrom("contact@socialbook.com");
        helper.setTo(to);
        helper.setSubject(subject);

        String template = templateEngine.process(templateName,context);
        helper.setText(template,true);
        javaMailSender.send(mimeMessage);

    }




}
