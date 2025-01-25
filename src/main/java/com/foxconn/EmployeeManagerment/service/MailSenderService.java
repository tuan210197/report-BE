package com.foxconn.EmployeeManagerment.service;


import org.springframework.stereotype.Service;

@Service
public interface MailSenderService {

    void sendSimpleMessage(String to, String subject, String text);
}