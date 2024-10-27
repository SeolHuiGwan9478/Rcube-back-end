package hufs.ces.rcube.domain.post.service;

import hufs.ces.rcube.domain.member.entity.Member;
import hufs.ces.rcube.domain.member.repository.MemberRepository;

import hufs.ces.rcube.domain.post.dto.ProjectRequestDto;
import hufs.ces.rcube.domain.post.dto.ProjectResponseDto;

import hufs.ces.rcube.domain.post.entity.Project;
import hufs.ces.rcube.domain.post.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ProjectResponseDto saveProject(ProjectRequestDto projectRequestDto){
        Member author = memberRepository.findByName(projectRequestDto.getAuthor());
        if (author == null) {
            throw new IllegalArgumentException("Author not found");
        }
        Project post = Project.builder()
                .projectName(projectRequestDto.getProjectName())
                .description(projectRequestDto.getDescription())
                .year(projectRequestDto.getYear())
                .imageUrl(projectRequestDto.getImageUrl())
                .projectLink(projectRequestDto.getProjectLink())
                .author(author)
                .build();
        Project savedPost = projectRepository.save(post);
        return  ProjectResponseDto.convertToProjectResponseDto(savedPost, "Post created successfully", HttpStatus.CREATED);



}

    // 모든 프로젝트 조회
    public ProjectResponseDto getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        return ProjectResponseDto.convertToProjectsResponseDto(projects, "모든 프로젝트 조회 성공", HttpStatus.OK);
    }

    // ID로 특정 프로젝트 조회
    public ProjectResponseDto getProjectById(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("프로젝트를 찾을 수 없습니다."));

        return ProjectResponseDto.convertToProjectResponseDto(project, "프로젝트 조회 성공", HttpStatus.OK);
    }


    // 연도별 프로젝트 조회
    public ProjectResponseDto getProjectByYear(int year) {
        List<Project> projects = projectRepository.findByYear(year);
        return ProjectResponseDto.convertToProjectsResponseDto(projects, year + "년도의 프로젝트 조회 성공", HttpStatus.OK);
    }

    @Transactional
    public ProjectResponseDto updateProject(Long id, ProjectRequestDto projectRequestDto){
        Project existingPost = projectRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Post not found with id: " + id));
        Member author = memberRepository.findByName(projectRequestDto.getAuthor());
        if (author == null) {
            throw new IllegalArgumentException("Author not found");
        }
    Project updatePost = Project.builder()
            .id(existingPost.getId())
            .projectName(projectRequestDto.getProjectName())
            .description(projectRequestDto.getDescription())
            .year(projectRequestDto.getYear())
            .imageUrl(projectRequestDto.getImageUrl())
            .projectLink(projectRequestDto.getProjectLink())
            .author(author)
            .build();
        Project savedPost = projectRepository.save(updatePost);
        return ProjectResponseDto.convertToProjectResponseDto(savedPost, "Post updated successfully", HttpStatus.OK);
    }

    @Transactional
    public void deleteProjectById(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new IllegalArgumentException("Post not found with id: " + id);
        }
        projectRepository.deleteById(id);
    }



}
