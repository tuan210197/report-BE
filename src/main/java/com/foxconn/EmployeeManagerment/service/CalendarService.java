package com.foxconn.EmployeeManagerment.service;

import com.foxconn.EmployeeManagerment.dto.request.OptionDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface CalendarService {
    boolean update(OptionDTO option);

    boolean importExcel(MultipartFile file) throws IOException;
}
