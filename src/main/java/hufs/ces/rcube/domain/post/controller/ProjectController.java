package hufs.ces.rcube.domain.post.controller;

import hufs.ces.rcube.domain.post.dto.ProjectRequestDto;
import hufs.ces.rcube.domain.post.dto.ProjectResponseDto;
import hufs.ces.rcube.domain.post.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectResponseDto> createProject(@RequestBody ProjectRequestDto projectRequestDto) {
        ProjectResponseDto createdProject = projectService.saveProject(projectRequestDto);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ProjectResponseDto> getAllProjects() {
        ProjectResponseDto project = projectService.getAllProjects();
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDto> getProjectById(@PathVariable Long id) {
        ProjectResponseDto project = projectService.getProjectById(id);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @GetMapping("{year}")
    public ResponseEntity<ProjectResponseDto> getProjectByYear(@PathVariable int year) {
        ProjectResponseDto project = projectService.getProjectByYear(year);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponseDto> updateProject(@PathVariable Long id, @RequestBody ProjectRequestDto projectRequestDto) {
        ProjectResponseDto project = projectService.updateProject(id, projectRequestDto);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    // ID로 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProjectById(@PathVariable Long id) {
        projectService.deleteProjectById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
