package hufs.ces.rcube.domain.post.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProjectTechStack {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "PROJECT_ID")
    private Project project; //해당 기술 스택이 사용된 프로젝트

    @ManyToOne
    @JoinColumn(name = "TECHSTACK_ID")
    private TeckStack teckStack;



}
