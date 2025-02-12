package com.foxconn.EmployeeManagerment.controller;

import com.foxconn.EmployeeManagerment.entity.Users;
import com.foxconn.EmployeeManagerment.projection.UserProjection;
import com.foxconn.EmployeeManagerment.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
public class UserController extends BaseController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/get-staff")
    public ResponseEntity<?> getStaff() {
       String uid = this.getCurrentUser().getUid().trim();
        List<UserProjection> users = userRepository.getUser(uid);
        return ResponseEntity.ok(users);
    }
    @GetMapping("/get-current")
    public ResponseEntity<?> getCurrent() {
        String uid = this.getCurrentUser().getUid().trim();
        return ResponseEntity.ok(userRepository.findByUid(uid));
}
}
