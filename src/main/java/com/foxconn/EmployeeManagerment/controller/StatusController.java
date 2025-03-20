package com.foxconn.EmployeeManagerment.controller;

import com.foxconn.EmployeeManagerment.common.Const;
import com.foxconn.EmployeeManagerment.entity.Status;
import com.foxconn.EmployeeManagerment.service.StatusService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/status")
public class StatusController extends BaseController {

    private final StatusService statusService;

    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAll() {
        List<Status> list = statusService.getStatus();
        if (list.isEmpty()) {
            return toExceptionResult(null, Const.API_RESPONSE.RETURN_CODE_ERROR);
        } else {
            return toSuccessResult(list, "SUCCESS");
        }
    }

}
