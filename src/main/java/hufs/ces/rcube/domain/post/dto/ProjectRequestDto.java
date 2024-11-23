package hufs.ces.rcube.domain.post.dto;

import lombok.*;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequestDto {
    private Long id;
    private String author;
    private String projectName;
    private String description;
    private int year; // 프로젝트 연도
    private String imageUrl;
    private String projectLink;
}
