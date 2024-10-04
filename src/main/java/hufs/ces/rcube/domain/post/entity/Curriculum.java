package hufs.ces.rcube.domain.post.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Curriculum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String month; // 커리큘럼이 적용되는 월
    private String title; // 커리큘럼 제목
    private String description; // 커리큘럼 설명





}
