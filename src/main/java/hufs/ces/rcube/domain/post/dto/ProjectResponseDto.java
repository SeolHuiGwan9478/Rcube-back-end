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
        private String name;
        private String description;
        private int year; // 프로젝트 연도
        private String imageUrl;
        private String projectLink;
    }

    // 여러 개의 Project를 변환하는 메서드
    public static ProjectResponseDto convertToProjectResponseDto(List<Project> projects, String message, HttpStatus status) {
        List<ProjectData> projectDataList = projects.stream()
                .map(project -> ProjectResponseDto.ProjectData.builder()
                        .id(project.getId())
                        .name(project.getName())
                        .description(project.getDescription())
                        .year(project.getYear())
                        .imageUrl(project.getImageUrl())
                        .projectLink(project.getProjectLink())
                        .build())
                .collect(Collectors.toList());

        return ProjectResponseDto.builder()
                .message(message)
                .data(projectDataList)
                .build();
    }
}
