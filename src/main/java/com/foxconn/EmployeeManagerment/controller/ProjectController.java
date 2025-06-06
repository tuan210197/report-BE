package com.foxconn.EmployeeManagerment.controller;


import com.foxconn.EmployeeManagerment.common.Const;
import com.foxconn.EmployeeManagerment.dto.request.FromDateToDateDTO;
import com.foxconn.EmployeeManagerment.dto.request.FromToDTO;
import com.foxconn.EmployeeManagerment.dto.request.ProjectDTO;
import com.foxconn.EmployeeManagerment.dto.request.ProjectUpdateDTO;
import com.foxconn.EmployeeManagerment.entity.Project;
import com.foxconn.EmployeeManagerment.repository.ProjectRepository;
import com.foxconn.EmployeeManagerment.service.ProjectService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/api/project")
public class ProjectController extends BaseController {


    private final ProjectService projectService;

    private final ProjectRepository projectRepository;


    @Autowired
    public ProjectController(ProjectService projectService, ProjectRepository projectRepository) {
        this.projectService = projectService;
        this.projectRepository = projectRepository;
    }


    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<?> createProject(HttpServletRequest request, @RequestBody ProjectDTO projectDto) throws Exception {
        try {
            UserDetails auth = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("GD"))) {
                Long projectId = projectService.createProject(projectDto);
                if (projectId != null) {
                    return toSuccessResult(projectId, "CREATE PROJECT SUCCESS");
                } else {
                    return toExceptionResult("CREATE FAIL", Const.API_RESPONSE.RETURN_CODE_ERROR);
                }
            }else {
                return toExceptionResult("NO PERMISSION", Const.API_RESPONSE.RETURN_CODE_ERROR);
            }

        } catch (IllegalArgumentException e) {
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_ERROR);
        } catch (Exception e) {
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.SYSTEM_CODE_ERROR);
        }
    }

    @PostMapping(value = "/update")
    public ResponseEntity<?> updateProject(HttpServletRequest request, @RequestBody ProjectDTO projectDTO) {
        try {
            String uid = this.getCurrentUser().getUid().trim();
            if (projectService.updateProject(uid, projectDTO)) {
                return toSuccessResult(null, "UPDATE PROJECT SUCCESS");
            } else {
                return toExceptionResult("UPDATE FAIL", Const.API_RESPONSE.RETURN_CODE_ERROR);
            }
        } catch (IllegalArgumentException e) {
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_ERROR);
        } catch (Exception e) {
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.SYSTEM_CODE_ERROR);
        }
    }




    @GetMapping(value = "/get-all")
    public ResponseEntity<?> getAllProject(HttpServletRequest request) {
        UserDetails auth = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("GD"))) {
            List<Project> projects = projectService.getAllProject();
            return toSuccessResult(projects, "SUCCESS");
        } else {
            return toExceptionResult("NO PERMISSION", Const.API_RESPONSE.RETURN_CODE_ERROR_NOTFOUND);
        }
    }

    // get all project by user id
    @GetMapping(value = "/get-by-userid")
    public ResponseEntity<?> getByUserId(HttpServletRequest request) {
        List<Project> projects = projectService.findProjectByUserId();
        return ResponseEntity.ok(projects);

    }

    // get project by projectId
    @GetMapping(value = "/{projectId}")
    public ResponseEntity<?> getProjectById(HttpServletRequest request, @PathVariable Long projectId) {
        Project project = projectRepository.findByProjectId(projectId);
        if (project != null) {
            return toSuccessResult(project, "SUCCESS");
        } else {
            return toExceptionResult("PROJECT NOT FOUND", Const.API_RESPONSE.RETURN_CODE_ERROR_NOTFOUND);
        }
    }

    @GetMapping(value = "/check-project/{projectId}")
    public ResponseEntity<?> checkOwnerProject(HttpServletRequest request, @PathVariable Long projectId) {
        String uid = this.getCurrentUser().getUid().trim();
        if (projectService.checkOwnerProject(uid, projectId)) {
            return toSuccessResult(null, "HAVE PERMISSION");
        } else {
            return toExceptionResult("NO PERMISSION", Const.API_RESPONSE.RETURN_CODE_ERROR);
        }

    }

    @GetMapping(value = "/dashboard")
    public ResponseEntity<?> getDashboard(HttpServletRequest request) {
        UserDetails auth = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(projectService.getDashboard());
    }

    @PostMapping(value = "/dashboard-from-to")
    public ResponseEntity<?> getDashboardFromTo(@RequestBody FromToDTO dto) {
        return ResponseEntity.ok(projectService.getDashboardFromTo(dto.getFrom(), dto.getTo()));
    }

    @PostMapping(value = "/dashboard-from-date-to-date")
    public ResponseEntity<?> getDashboardFromDateToDate(@RequestBody FromDateToDateDTO dto) {
        return ResponseEntity.ok(projectService.getDashboardFromDateToDate(dto.getFrom(), dto.getTo()));
    }


    @GetMapping("/get-total")
    public ResponseEntity<?> getTotal(HttpServletRequest request) {
        return ResponseEntity.ok(projectService.getTotal());
    }

    @GetMapping("/get-completed")
    public ResponseEntity<?> getCompleted(HttpServletRequest request) {
        return ResponseEntity.ok(projectService.getCompleted());
    }

    @GetMapping("/get-completed2")
    public ResponseEntity<?> getCompleted2(HttpServletRequest request) {
        return ResponseEntity.ok(projectService.getCompleted2());
    }

    @PostMapping("/get-from-to")
    public ResponseEntity<?> getFromTo(HttpServletRequest request, @RequestBody FromToDTO dto) {
        return ResponseEntity.ok(projectService.getTotalFromTo(dto.getFrom(), dto.getTo()));
    }

    @PostMapping("/get-from-date-to-date")
    public ResponseEntity<?> getFromDateToDate(HttpServletRequest request, @RequestBody FromDateToDateDTO dto) {
        return ResponseEntity.ok(projectService.getTotalFromTo(dto.getFrom(), dto.getTo()));
    }

    @GetMapping("/get-project-name")
    public ResponseEntity<?> getProjectName(HttpServletRequest request) {
        return ResponseEntity.ok(projectService.getProjectName());
    }

    @PostMapping("/update-status")
    public ResponseEntity<?> updateStatus(HttpServletRequest request, @RequestBody ProjectUpdateDTO dto) {
        try {
            String uid = this.getCurrentUser().getUid().trim();
            if (projectService.updateStatus(dto, uid)) {
                return toSuccessResult(null, "UPDATE SUCCESS");
            } else {
                return toExceptionResult("UPDATE FALSE", Const.API_RESPONSE.RETURN_CODE_ERROR);
            }
        }catch (IllegalArgumentException e) {
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_ERROR);
        } catch (Exception e) {
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.SYSTEM_CODE_ERROR);
        }
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(HttpServletRequest request, @RequestBody ProjectDTO projectDTO) {
        return ResponseEntity.ok(projectService.search(projectDTO.getProjectName()));
    }

    @PostMapping("/search-chart")
    public ResponseEntity<?> searchChart(HttpServletRequest request, @RequestBody ProjectDTO projectDTO) {
        List<Project> projects = projectService.searchChart(projectDTO);
        if (!projects.isEmpty()) {
            return toSuccessResult(projects, "SUCCESS");
        } else {
            return toExceptionResult("NO DATA", Const.API_RESPONSE.RETURN_CODE_ERROR);
        }
    }

    @PostMapping("/search-chart-from-to")
    public ResponseEntity<?> searchChartFromTo(HttpServletRequest request, @RequestBody FromToDTO dto) {
        List<Project> projects = projectService.searchChartFromTo(dto);
        if (!projects.isEmpty()) {
            return toSuccessResult(projects, "SUCCESS");
        } else {
            return toExceptionResult("NO DATA", Const.API_RESPONSE.RETURN_CODE_ERROR);
        }
    }

    @PostMapping("/search-chart-from-date-to-date")
    public ResponseEntity<?> searchChartFromDateToDate(HttpServletRequest request, @RequestBody FromDateToDateDTO dto) {
        List<Project> projects = projectService.searchChartFromTo(dto);
        if (!projects.isEmpty()) {
            return toSuccessResult(projects, "SUCCESS");
        } else {
            return toExceptionResult("NO DATA", Const.API_RESPONSE.RETURN_CODE_ERROR);
        }
    }



    @PostMapping("/search-by-name")
    public ResponseEntity<?> searchByName(HttpServletRequest request, @RequestBody ProjectDTO projectDTO) {
        List<Project> projects = projectService.getProjectByName(projectDTO.getProjectName());
        return toSuccessResult(projects, "SUCCESS");

    }

    @PostMapping("/delete")
    public ResponseEntity<?> delete(HttpServletRequest request, @RequestBody ProjectDTO projectDTO) {
        UserDetails auth = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String uid = this.getCurrentUser().getUid().trim();
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(Const.ROLE.GD))) {
            if (projectService.deleteProject(projectDTO.getProjectId(), uid)) {
                return toSuccessResult(null, "DELETE SUCCESS");
            } else {
                return toExceptionResult("DELETE FALSE", Const.API_RESPONSE.RETURN_CODE_ERROR);
            }
        } else {
            return toExceptionResult("NO PERMISSION", Const.API_RESPONSE.RETURN_CODE_ERROR);
        }
    }
}
