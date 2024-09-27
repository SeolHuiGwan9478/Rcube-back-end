package hufs.ces.rcube.domain.post.service;

import hufs.ces.rcube.domain.post.dto.ProjectResponseDto;
import hufs.ces.rcube.domain.post.entity.Project;
import hufs.ces.rcube.domain.post.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;

    // 모든 프로젝트 조회
    public ProjectResponseDto getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        return ProjectResponseDto.convertToProjectResponseDto(projects, "모든 프로젝트 조회 성공", HttpStatus.OK);
    }

    // ID로 특정 프로젝트 조회
    public ProjectResponseDto getProjectById(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("프로젝트를 찾을 수 없습니다."));

        // 단일 프로젝트를 리스트로 감싸서 convertToPostResponseDto 메서드 호출
        return ProjectResponseDto.convertToProjectResponseDto(List.of(project), "프로젝트 조회 성공", HttpStatus.OK);
    }


    // 연도별 프로젝트 조회
    public ProjectResponseDto getProjectByYear(int year) {
        List<Project> projects = projectRepository.findByYear(year);
        return ProjectResponseDto.convertToProjectResponseDto(projects, year + "년도의 프로젝트 조회 성공", HttpStatus.OK);
    }



}
