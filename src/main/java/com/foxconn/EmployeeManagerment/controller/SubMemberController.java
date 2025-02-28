package com.foxconn.EmployeeManagerment.controller;

import com.foxconn.EmployeeManagerment.common.Const;
import com.foxconn.EmployeeManagerment.dto.request.SubMemberDTO;
import com.foxconn.EmployeeManagerment.entity.SubMember;
import com.foxconn.EmployeeManagerment.service.SubMemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@CrossOrigin
@RequestMapping("/api/sub-member")
public class SubMemberController extends BaseController {
    private final SubMemberService subMemberService;

    public SubMemberController(SubMemberService subMemberService) {
        this.subMemberService = subMemberService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> createSubMember(@RequestBody SubMemberDTO subMember) {
        try {
            if (subMemberService.createSubMember(subMember)) {
                return toSuccessResult(null, "oke");
            } else {
                return toExceptionResult("LỖI", Const.API_RESPONSE.RETURN_CODE_ERROR);
            }
        } catch (IllegalArgumentException e) {
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_ERROR);
        } catch (Exception e) {
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.SYSTEM_CODE_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<SubMember> updateSubMember(@PathVariable Long id, @RequestBody SubMember subMember) {
        SubMember updatedSubMember = subMemberService.updateSubMember(id, subMember);
        return ResponseEntity.ok(updatedSubMember);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteSubMember(@PathVariable Long id) {
        subMemberService.deleteSubMember(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<SubMember> getSubMemberById(@PathVariable Long id) {
        SubMember subMember = subMemberService.getSubMemberById(id);
        return ResponseEntity.ok(subMember);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<SubMember>> getAllSubMembers() {
        List<SubMember> subMembersList = subMemberService.getAllSubMembers();
        return ResponseEntity.ok(subMembersList);
    }
    @GetMapping("/get-by-user")
    public ResponseEntity<?> getSubMemberByUser(HttpServletRequest request) {
     //   UserDetails auth = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String uid = this.getCurrentUser().getUid().trim();
        System.out.println(uid);
        return ResponseEntity.ok(subMemberService.getProjectByUserId(uid));
    }
}
