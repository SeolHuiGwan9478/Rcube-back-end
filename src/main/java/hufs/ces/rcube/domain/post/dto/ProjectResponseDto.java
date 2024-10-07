package hufs.ces.rcube.domain.post.dto;

import hufs.ces.rcube.domain.post.entity.Project;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ProjectResponseDto {
    private String message; // 응답 메시지
    private List<ProjectData> data; // 여러 프로젝트를 담기 위해 List로 변경

    @Getter
    @Builder
    public static class ProjectData {
        private Long id;
        private String author;
        private String projectName;
        private String description;
        private int year; // 프로젝트 연도
        private String imageUrl;
        private String projectLink;
    }

    //Project를 변환하는 메서드
    public static ProjectResponseDto convertToProjectResponseDto(Project project, String message, HttpStatus status) {
        ProjectData projectData = ProjectData.builder()
                        .id(project.getId())
                        .projectName(project.getProjectName())
                        .description(project.getDescription())
                        .year(project.getYear())
                        .imageUrl(project.getImageUrl())
                        .projectLink(project.getProjectLink())
                        .build();


        return ProjectResponseDto.builder()
                .message(message)
                .data(List.of(projectData))
                .build();
    }
    // 다중 Project를 변환하는 메서드
    public static ProjectResponseDto convertToProjectsResponseDto(List<Project> projects, String message, HttpStatus status) {
        List<ProjectData> projectDataList = projects.stream()
                .map(project -> ProjectData.builder()
                        .id(project.getId())
                        .projectName(project.getProjectName())
                        .description(project.getDescription())
                        .year(project.getYear())
                        .imageUrl(project.getImageUrl())
                        .projectLink(project.getProjectLink())
                        .build())
                .collect(Collectors.toList());

        return ProjectResponseDto.builder()
                .message(message)
                .data(projectDataList) // 리스트 데이터를 설정
                .build();
    }
}

