package com.foxconn.EmployeeManagerment.controller;

import com.foxconn.EmployeeManagerment.projection.MinMaxYearProjection;
import com.foxconn.EmployeeManagerment.repository.ProjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.AbstractController;

@RestController
@Slf4j
@CrossOrigin
@RequestMapping("/api/home")
public class HomeController extends BaseController {

    private final ProjectRepository projectRepository;

    public HomeController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @GetMapping("/get-year")
    public ResponseEntity<?> getYear() {
        MinMaxYearProjection minMaxYearProjection = projectRepository.getMinMaxYear();
        if (minMaxYearProjection == null) {
            return toSuccessResult(null,"NO YEAR");
        }else{
            return toSuccessResult(minMaxYearProjection,"SUCCESS");
        }
    }

}
