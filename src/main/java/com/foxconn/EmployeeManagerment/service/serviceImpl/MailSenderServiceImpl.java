package com.foxconn.EmployeeManagerment.service.serviceImpl;

import com.foxconn.EmployeeManagerment.service.MailSenderService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public void sendReportEmail(List<String> to, String subject, String text) {
//        SimpleMailMessage message = new SimpleMailMessage();
        MimeMessage message = emailSender.createMimeMessage();
//        message.setFrom(mailServerUsername);
//        message.setTo(to.toArray(new String[0]));
//        message.setSubject(subject);
//        message.setText(text);
//        emailSender.send(message);
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(mailServerUsername);
            helper.setTo(to.toArray(new String[0])); // Chuyển List<String> thành mảng
            helper.setSubject(subject);
            helper.setText(text, true); // **Chú ý: true để gửi HTML**

            emailSender.send(message);
            System.out.println("✅ Email đã gửi thành công!");

        } catch (MessagingException e) {

            System.out.println("❌ Gửi email thất bại!");
        }
    }
//    Session session = Session.getInstance(prop,
//            new javax.mail.Authenticator() {
//                protected PasswordAuthentication getPasswordAuthentication() {
//                    return new PasswordAuthentication(username, appPassword);
//                }
//            });

}