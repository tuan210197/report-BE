package com.foxconn.EmployeeManagerment.controller;

import com.foxconn.EmployeeManagerment.common.Const;
import com.foxconn.EmployeeManagerment.dto.request.FromDateToDateDTO;
import com.foxconn.EmployeeManagerment.dto.request.FromToDTO;
import com.foxconn.EmployeeManagerment.dto.request.OptionDTO;
import com.foxconn.EmployeeManagerment.entity.Calendar;
import com.foxconn.EmployeeManagerment.entity.Users;
import com.foxconn.EmployeeManagerment.projection.CalendarProjection;
import com.foxconn.EmployeeManagerment.repository.CalendarRepository;
import com.foxconn.EmployeeManagerment.service.CalendarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/calendar")
public class CalendarController extends BaseController {

    private final CalendarRepository calendarRepository;
    private final CalendarService calendarService;

    public CalendarController(CalendarRepository calendarRepository, CalendarService calendarService) {
        this.calendarRepository = calendarRepository;
        this.calendarService = calendarService;
    }

    @PostMapping("/get-all")
    public ResponseEntity<?> getAll(@RequestBody FromDateToDateDTO dto) {
//        List<Calendar> list = calendarRepository.findAll();
        try {
            LocalDate from = LocalDate.parse(dto.getFrom());
            LocalDate to = LocalDate.parse(dto.getTo());
            List<CalendarProjection> list = calendarRepository.getCalendarByMonth(from, to);
            if (list != null) {
                return ResponseEntity.status(HttpStatus.OK).body(list);
            } else {
                return toExceptionResult(null, Const.API_RESPONSE.RETURN_CODE_ERROR_NOTFOUND);
            }
        } catch (IllegalArgumentException e) {
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_ERROR);
        } catch (Exception e) {
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.SYSTEM_CODE_ERROR);
        }
    }

    @GetMapping("/get-user-by-id/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Users user = calendarRepository.findById(id).get().getUser();
        if (user != null) {
            return toSuccessResult(user, "success");
        } else {
            return toExceptionResult(null, Const.API_RESPONSE.RETURN_CODE_ERROR_NOTFOUND);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody OptionDTO option) {
        try {
            if (calendarService.update(option)) {
                return toSuccessResult(null, "Successfully updated");
            } else {
                return toExceptionResult(null, Const.API_RESPONSE.RETURN_CODE_ERROR);
            }
        } catch (IllegalArgumentException e) {
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_ERROR);
        } catch (Exception e) {
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.SYSTEM_CODE_ERROR);
        }
    }

    @PostMapping("/import")
    public ResponseEntity<?> importExcel(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File không hợp lệ.");
        }
        try {
            if (calendarService.importExcel(file)) {
                return toSuccessResult(null, "Successfully imported");
            } else {
                return toExceptionResult(null, Const.API_RESPONSE.RETURN_CODE_ERROR);
            }
        } catch (IllegalArgumentException e) {
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_ERROR);
        } catch (Exception e) {
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.SYSTEM_CODE_ERROR);
        }
    }
}
