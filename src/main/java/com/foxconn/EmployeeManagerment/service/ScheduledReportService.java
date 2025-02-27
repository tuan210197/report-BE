package com.foxconn.EmployeeManagerment.service;

import com.foxconn.EmployeeManagerment.entity.BasicLogin;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

@Service
public class ScheduledReportService {


    private final MailSenderService mailSenderService;

    public ScheduledReportService(MailSenderService mailSenderService) {
        this.mailSenderService = mailSenderService;
    }

    //    @Scheduled(fixedRate = 6000) // Gọi API mỗi 60 giây
    public void sendEmailReport() {
        try{


            System.out.println("Sending email report");


        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }


}
