package com.foxconn.EmployeeManagerment.service;

import com.foxconn.EmployeeManagerment.entity.BasicLogin;
import com.foxconn.EmployeeManagerment.projection.EmailReportProjection;
import com.foxconn.EmployeeManagerment.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ScheduledReportService {


    private final MailSenderService mailSenderService;
    private final UserRepository userRepository;

    public ScheduledReportService(MailSenderService mailSenderService, UserRepository userRepository) {
        this.mailSenderService = mailSenderService;
        this.userRepository = userRepository;
    }

    //    @Scheduled(fixedRate = 60000) // Gọi API mỗi 60 giây
//    @Scheduled(cron = "0 0 15 * * 1-6", zone = "Asia/Ho_Chi_Minh")
    public void sendEmailReport() {
        try {
            List<String> listEmail = userRepository.getUserEmail();
            String email = String.join(",", listEmail);
            log.info("Sending email report");
//            sendEmail(listEmail, "每日报告");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void sendEmail(List<String> email, String subject) {
        List<EmailReportProjection> list = userRepository.getUserReport();
        mailSenderService.sendReportEmail(email, subject, generateReportTable(list));
    }

    public String generateReportTable(List<EmailReportProjection> reports) {
        StringBuilder html = new StringBuilder();
        html.append("<html><body>");
        html.append("<h2>今天的报告清单</h2>");
        html.append("<table border='1' cellspacing='0' cellpadding='8' style='border-collapse: collapse; width: 100%; max-width: 600px'>");
        html.append("<tr style='background-color: #f2f2f2;'>");
        html.append("<th style='border: 1px solid black;'>全名</th>");
        html.append("<th style='border: 1px solid black;'>地位</th>");
        html.append("</tr>");

        for (EmailReportProjection report : reports) {
            html.append("<tr>");
            html.append("<td style='border: 1px solid black; padding-left: 10px;'>").append(report.getFullName()).append("</td>");
//            html.append("<td style='border: 1px solid black; padding-left: 10px;'>").append(report.getReport()).append("</td>");
            html.append("<td style='border: 1px solid black; text-align: center; font-weight: bold; color: ").append(report.getReport().equals("已报告") ? "green" : "red").append(";'>").append(report.getReport()).append("</td>");
            html.append("</tr>");
        }
        html.append("</table>");
        html.append("</body></html>");
        return html.toString();
    }


}
