package com.foxconn.EmployeeManagerment.service;

import com.foxconn.EmployeeManagerment.projection.EmailReportProjection;
import com.foxconn.EmployeeManagerment.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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

//        @Scheduled(fixedRate = 600000000) // Gọi API mỗi 60 giây
//    @Scheduled(cron = "0 0 15 * * 1-6", zone = "Asia/Ho_Chi_Minh")
    public void sendEmailReport() {
        try {
            List<String> listEmail = userRepository.getUserEmail();
            String email = String.join(",", listEmail);

            listEmail.forEach(System.out::println);

//            sendEmail(listEmail, "越南資訊部每日專案進度報告");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void sendEmail(List<String> email, String subject) {
        List<EmailReportProjection> list = userRepository.getUserReport();
        System.out.println(list.get(0).getReport());
        mailSenderService.sendReportEmail(email, subject, generateReportTable(list));
    }

    public String generateReportTable(List<EmailReportProjection> reports) {
        StringBuilder html = new StringBuilder();
        html.append("<html><body>");
        html.append("<h3>尊敬的先生/小姐：</h3>");
        html.append("<h4 style=\"font-weight: lighter;\">您好，這是Fii越南資訊部的人員報告進度通知！</h4>");
        html.append("<h4 style=\"font-weight: lighter;\"> 您可以點擊以下鏈接進行查看。</h4>");
        html.append(" <h4 style=\"font-weight: lighter;\">     點擊鏈接(內網): <a href=\"http://10.81.160.29:86/detail-report\" style=\"font-weight: bold;\">专案系统</a> </h4>");
        html.append("<h4 style=\"font-weight: lighter;\"> 若您有疑問或遇到系統不方便使用的地方，請聯繫我們，謝謝！</h4> <br> <br>");
        html.append("<table border='1' cellspacing='0' cellpadding='8' style='border-collapse: collapse; width: 100%; max-width: 600px'>");
        html.append("<tr style='background-color: #f2f2f2;'>");
        html.append("<th style='border: 1px solid black;'>姓名</th>");
        html.append("<th style='border: 1px solid black;'> 報告狀態</th>");
        html.append("</tr>");

        for (EmailReportProjection report : reports) {
            html.append("<tr>");
            html.append("<td style='border: 1px solid black; padding-left: 10px;'>").append(report.getFullName()).append("</td>");
//            html.append("<td style='border: 1px solid black; padding-left: 10px;'>").append(report.getReport()).append("</td>"); 已報告/已报告
            html.append("<td style='border: 1px solid black; text-align: center; font-weight: bold; color: ").append(report.getReport().equals("已報告") ? "green" : "red").append(";'>").append(report.getReport()).append("</td>");
            html.append("</tr>");
        }
        html.append("</table>");
        html.append("</body></html>");
        return html.toString();
    }


}
