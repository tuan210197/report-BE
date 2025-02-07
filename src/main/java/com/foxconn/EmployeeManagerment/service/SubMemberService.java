package com.foxconn.EmployeeManagerment.service;

import com.foxconn.EmployeeManagerment.dto.request.SubMemberDTO;
import com.foxconn.EmployeeManagerment.entity.SubMember;

import java.util.List;

public interface SubMemberService {
    boolean createSubMember(SubMemberDTO subMember);
    SubMember updateSubMember(Long id, SubMember subMember);
    void deleteSubMember(Long id);
    SubMember getSubMemberById(Long id);
    List<SubMember> getAllSubMembers();

    List<SubMember> getProjectByUserId(String userId);
}
