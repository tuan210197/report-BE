package com.foxconn.EmployeeManagerment.controller;

import com.foxconn.EmployeeManagerment.common.Const;
import com.foxconn.EmployeeManagerment.dto.request.ImplementDto;
import com.foxconn.EmployeeManagerment.entity.Implement;
import com.foxconn.EmployeeManagerment.entity.Users;
import com.foxconn.EmployeeManagerment.service.ImplementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/implement")
@CrossOrigin
@Slf4j
public class ImplementController extends BaseController {

    private final ImplementService implementService;

    public ImplementController(ImplementService implementService) {
        this.implementService = implementService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> createImplement(@RequestBody ImplementDto implement) {
        String uid = this.getCurrentUser().getUid().trim();
        if (implementService.createImplement(implement, uid)) {
            return toSuccessResult(null, "Add Success");
        } else {
            return toExceptionResult("Add Fail", Const.API_RESPONSE.RETURN_CODE_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Implement> updateImplement(@PathVariable Long id, @RequestBody Implement implement) {
        Implement updatedImplement = implementService.updateImplement(id, implement);
        return ResponseEntity.ok(updatedImplement);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteImplement(@PathVariable Long id) {
        implementService.deleteImplement(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getImplementById(@PathVariable Long id) {
        List<Implement> implement = implementService.getImplementById(id);
        return ResponseEntity.ok(implement);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<Implement>> getAllImplements() {
        List<Implement> implementsList = implementService.getAllImplements();
        return ResponseEntity.ok(implementsList);
    }

    @GetMapping("/get-by-user/{userImplement}")
    public ResponseEntity<List<Implement>> getImplementsByUserImplement(@PathVariable String userImplement) {
        List<Implement> implementsList = implementService.getImplementsByUserImplement(userImplement);
        return ResponseEntity.ok(implementsList);
    }

    @PostMapping("/get-implement-by-project")
    public ResponseEntity<?> getImplementsByProject(@RequestBody Long projectId) {
        List<String> implement = implementService.getImplementByProject(projectId);
        if (implement != null) {
            return toSuccessResult(implement, "SUCCESS");
        } else {
            return toExceptionResult("No implementation found", Const.API_RESPONSE.RETURN_CODE_ERROR);
        }
    }

}
