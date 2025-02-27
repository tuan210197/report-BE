package com.foxconn.EmployeeManagerment.service.serviceImpl;

import com.foxconn.EmployeeManagerment.dto.request.ImplementDto;
import com.foxconn.EmployeeManagerment.entity.Implement;
import com.foxconn.EmployeeManagerment.entity.Project;
import com.foxconn.EmployeeManagerment.entity.Users;
import com.foxconn.EmployeeManagerment.repository.ImplementRepository;
import com.foxconn.EmployeeManagerment.repository.ProjectRepository;
import com.foxconn.EmployeeManagerment.repository.UserRepository;
import com.foxconn.EmployeeManagerment.service.ImplementService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ImplementImpl implements ImplementService {

    private final ImplementRepository implementRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ImplementImpl(ImplementRepository implementRepository, ProjectRepository projectRepository, UserRepository userRepository) {
        this.implementRepository = implementRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }


    @Override
    public Boolean createImplement(ImplementDto implementDto, String userImplement) {

        Project project = projectRepository.findByProjectId(implementDto.getProjectId());
        Users users = userRepository.findByUid(userImplement);

        Implement implement = Implement.builder()
                .projects(project)
                .users(users)
                .implement(implementDto.getImplement())
                .createAt(LocalDateTime.now())
                .build();
         implementRepository.save(implement);
      return   Objects.nonNull(implement.getId());
    }

    @Override
    public Implement updateImplement(Long id, Implement implement) {
        Optional<Implement> existingImplement = implementRepository.findById(id);
//Users users = userRepository.findByUid(id)
        if (existingImplement.isPresent()) {
            Implement updatedImplement = existingImplement.get();
//            updatedImplement.setUserImplements();
            updatedImplement.setImplement(implement.getImplement());
            return implementRepository.save(updatedImplement);
        } else {
            throw new RuntimeException("Implement not found");
        }
    }

    @Override
    public void deleteImplement(Long id) {
        implementRepository.deleteById(id);
    }

    @Override
    public List<Implement> getImplementById(Long id) {
        return implementRepository.findByProjectId(id);
    }

    @Override
    public List<Implement> getAllImplements() {
        return implementRepository.findAll();
    }

    @Override
    public List<Implement> getImplementsByUserImplement(String userImplement) {
        return implementRepository.findByUsers(userImplement);
    }

    @Override
    public List<String> getImplementByProject(Long projectId) {
        List<Implement> list = implementRepository.getImplementByProjects(projectId);
        return list.stream().map(Implement::getImplement).toList();
    }

//    @Override
//    public List<Implement> getImplementsByProjectId(Long projectId) {
//        return implementRepository.findByProjectId(projectId);
//    }
}
