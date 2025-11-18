package com.enotes.Enotes_INDUS.service;


import com.enotes.Enotes_INDUS.dto.EmailRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.message;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String mailFrom;

        public void sendEmail(EmailRequest request) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message=   javaMailSender.createMimeMessage();
            MimeMessageHelper helper=new MimeMessageHelper(message);

            helper.setFrom(mailFrom, request.getTitle());
            helper.setTo(request.getTo());
            helper.setSubject(request.getSubject());
            helper.setText(request.getMessage(),true);

            javaMailSender.send(message);


        }

}
