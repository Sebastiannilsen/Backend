package no.ntnu.bicycle.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.ntnu.bicycle.model.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

/**
 * !TODO write documentation here
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSenderService{
    @Autowired
    private JavaMailSender javaMailSender;


    private final SpringTemplateEngine templateEngine;

    private static final String FROM_EMAIL = "keep.rolling.rolling.rolling16@gmail.com";


    public void sendEmail(String toEmail, String subject, String body) throws MailException {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(FROM_EMAIL);
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        javaMailSender.send(message);

        System.err.println("Message sent");

    }



    public void sendHtmlMessage(Email email) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        Context context = new Context();
        context.setVariables(email.getProperties());
        helper.setFrom(FROM_EMAIL);
        helper.setTo(email.getTo());
        helper.setSubject(email.getSubject());
        String html = templateEngine.process("/HTML/email/" + email.getTemplate(), context);
        helper.setText(html, true);

        javaMailSender.send(message);
    }
}
