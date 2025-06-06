package com.foxconn.EmployeeManagerment.service.serviceImpl;

import com.foxconn.EmployeeManagerment.dto.request.OptionDTO;
import com.foxconn.EmployeeManagerment.entity.Calendar;
import com.foxconn.EmployeeManagerment.entity.CalendarOption;
import com.foxconn.EmployeeManagerment.entity.Users;
import com.foxconn.EmployeeManagerment.repository.CalendarOptionRepository;
import com.foxconn.EmployeeManagerment.repository.CalendarRepository;
import com.foxconn.EmployeeManagerment.repository.UserRepository;
import com.foxconn.EmployeeManagerment.service.CalendarService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CalendarServiceImpl implements CalendarService {

    private final CalendarRepository calendarRepository;
    private final UserRepository userRepository;
    private final CalendarOptionRepository calendarOptionRepository;

    public CalendarServiceImpl(CalendarRepository calendarRepository, UserRepository userRepository, CalendarOptionRepository calendarOptionRepository) {
        this.calendarRepository = calendarRepository;
        this.userRepository = userRepository;
        this.calendarOptionRepository = calendarOptionRepository;
    }

    @Override
    @Transactional
    public boolean update(OptionDTO option) {
        Calendar calendar = calendarRepository.findById(option.getId()).orElseThrow(
                () -> new IllegalArgumentException("Invalid option id: " + option.getId())
        );
        Users user = userRepository.findByUid(option.getUid());
        String title = user.getFullName();
        CalendarOption calendarOption = calendarOptionRepository.findById(option.getTask()).orElseThrow(
                () -> new IllegalArgumentException("invalid option" + option.getTask())
        );
        calendar.setUser(user);
        calendar.setTitle(title);
        calendar.setTask(calendarOption);
        calendarRepository.save(calendar);
        return true;
    }

    @Override
    @Transactional
    public boolean importExcel(MultipartFile file) throws IOException {
        List<Calendar> validList = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        int rowIndex = 0;
        for (Row row : sheet) {
            if (rowIndex == 0 ) {
                rowIndex++;
                continue;
            } // skip header
            try {
                String uid = row.getCell(1).getStringCellValue();
                Users user = userRepository.findByEmployeeCode(uid);
                String title = "";

                LocalDate date = LocalDate.from(row.getCell(3).getLocalDateTimeCellValue());
                Calendar calendar = calendarRepository.findByDate(date);
                // Kiểm tra nếu ngày đã tồn tại trong DB
                if (calendar != null) {
                    errors.add("Dòng " + rowIndex );
                    throw new  IllegalArgumentException("Ngày: "+date + " đã tồn tại");
                }
                CalendarOption task = calendarOptionRepository.findById(1L).orElseThrow(
                        () -> new IllegalArgumentException("Invalid option id: " + 1)
                );
                if (user == null) {
                    title= task.getValue();
                }else {
                    title = user.getFullName();
                }
                if (uid == null || uid.isBlank()) throw new RuntimeException("UID rỗng");
                Calendar wl = new Calendar(user,title, date, task);
                validList.add(wl);
            } catch (Exception e) {
                errors.add("Dòng " + rowIndex + " lỗi: " + e.getMessage());
                log.info(e.getMessage());
            }
            rowIndex++;
        }
        if (errors.isEmpty()) {
            calendarRepository.saveAll(validList);
            return true;
        }else {
            return false;
        }

    }
}

