package com.foxconn.EmployeeManagerment.service;


import java.util.List;

public interface MailSenderService {

    void sendSimpleMessage(String to, String subject, String text);
    void sendReportEmail(List<String> to, String subject, String text);
}