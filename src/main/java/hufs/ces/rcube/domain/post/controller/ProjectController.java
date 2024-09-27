package hufs.ces.rcube.domain.post.controller;

import hufs.ces.rcube.domain.post.dto.PostResponseDto;
import hufs.ces.rcube.domain.post.dto.ProjectResponseDto;
import hufs.ces.rcube.domain.post.entity.Project;
import hufs.ces.rcube.domain.post.service.ProjectService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<ProjectResponseDto> getAllProjects(){
        try {
            ProjectResponseDto project = projectService.getAllProjects();
            return new ResponseEntity<>(project, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDto> getPostById(@PathVariable Long id) {
        try {
            ProjectResponseDto project = projectService.getProjectById(id);
            return new ResponseEntity<>(project, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("{year}")
    public ResponseEntity<ProjectResponseDto> getProjectByYear(@PathVariable int year){
        try {
            ProjectResponseDto project = projectService.getProjectByYear(year);
            return new ResponseEntity<>(project, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
