package com.foxconn.EmployeeManagerment.service.serviceImpl;

import com.foxconn.EmployeeManagerment.service.MailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailSenderServiceImpl implements MailSenderService {

    @Autowired
    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String mailServerUsername;

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailServerUsername);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
//    Session session = Session.getInstance(prop,
//            new javax.mail.Authenticator() {
//                protected PasswordAuthentication getPasswordAuthentication() {
//                    return new PasswordAuthentication(username, appPassword);
//                }
//            });

}