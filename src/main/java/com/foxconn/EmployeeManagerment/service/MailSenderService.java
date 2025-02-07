package com.foxconn.EmployeeManagerment.service;



public interface MailSenderService {

    void sendSimpleMessage(String to, String subject, String text);
}