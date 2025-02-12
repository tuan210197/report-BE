package com.foxconn.EmployeeManagerment.service.serviceImpl;

import com.foxconn.EmployeeManagerment.dto.request.SubMemberDTO;
import com.foxconn.EmployeeManagerment.entity.Project;
import com.foxconn.EmployeeManagerment.entity.SubMember;
import com.foxconn.EmployeeManagerment.entity.Users;
import com.foxconn.EmployeeManagerment.repository.ProjectRepository;
import com.foxconn.EmployeeManagerment.repository.SubMemberRepository;
import com.foxconn.EmployeeManagerment.repository.UserRepository;
import com.foxconn.EmployeeManagerment.service.MailSenderService;
import com.foxconn.EmployeeManagerment.service.SubMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class SubMemberImpl implements SubMemberService {

    private final SubMemberRepository subMemberRepository;
    private final ProjectRepository projectRepository;
    private final MailSenderService mailSenderService;
    private final UserRepository userRepository;


    public SubMemberImpl(SubMemberRepository subMemberRepository, ProjectRepository projectRepository, MailSenderService mailSenderService, UserRepository userRepository) {
        this.subMemberRepository = subMemberRepository;
        this.projectRepository = projectRepository;
        this.mailSenderService = mailSenderService;
        this.userRepository = userRepository;
    }

    @Override
    public boolean createSubMember(SubMemberDTO subMember) {
        Project project = projectRepository.findByProjectId(subMember.getProjectId());

        SubMember savedSubMember = SubMember.builder()
                .projectId(project)
                .user(subMember.getUser())
                .projectName(subMember.getProjectName())
                .build();
       savedSubMember=  subMemberRepository.save(savedSubMember);
    //   this.sendSubMember(savedSubMember,"ADD NEW PROJECT");
        return Objects.nonNull(savedSubMember.getId());
    }
    public void sendSubMember(SubMember subMember, String subject) {
          Users user = userRepository.findByUid(subMember.getUser());
          log.info("email: "+user.getEmail());
          String projectName = subMember.getProjectName();
          Long id = subMember.getProjectId().getProjectId();
        mailSenderService.sendSimpleMessage(user.getEmail(), subject,
                "Bạn Được Mời Vào Dự Án Mới" + "\n"
                        + "Tên Dự Án: "+ projectName + "\n"
                    + "Mã dự án: "+ id);


    }


    @Override
    public SubMember updateSubMember(Long id, SubMember subMember) {
        SubMember existingSubMember = subMemberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SubMember not found"));
        existingSubMember.setUser(subMember.getUser());
        existingSubMember.setProjectName(subMember.getProjectName());
        return subMemberRepository.save(existingSubMember);
    }

    @Override
    public void deleteSubMember(Long id) {
        subMemberRepository.deleteById(id);
    }

    @Override
    public SubMember getSubMemberById(Long id) {
        return subMemberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SubMember not found"));
    }

    @Override
    public List<SubMember> getAllSubMembers() {
        return subMemberRepository.findAll();
    }

    @Override
    public List<SubMember> getProjectByUserId(String userId) {
        return subMemberRepository.getProjectByUser(userId);
    }
}
