package com.foxconn.EmployeeManagerment.controller;

import com.foxconn.EmployeeManagerment.entity.Category;
import com.foxconn.EmployeeManagerment.repository.CateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cate")
@CrossOrigin
@Slf4j
public class CategoryController {

// CREATE API CRUD WITH ENTITY CATEGORY
    private final CateRepository cateRepository;

    @Autowired
    public CategoryController(CateRepository cateRepository) {
        this.cateRepository = cateRepository;
    }

    @GetMapping("/get-all")
    public List<Category> getAllCategories() {
        return cateRepository.findAll();
    }

}
