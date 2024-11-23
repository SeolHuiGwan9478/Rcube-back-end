package hufs.ces.rcube.domain.post.dto;
import hufs.ces.rcube.domain.post.entity.Curriculum;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class MainPageDto {
    private int totalProjects;
    private Map<String, List<Curriculum>> curriculumsByMonth; // 달별 커리큘럼
    private long totalMembers;
    private long currentMembers;
}

