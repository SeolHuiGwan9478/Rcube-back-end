package hufs.ces.rcube.domain.post.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TechStack {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; //기술 스택 이름

    @OneToMany(mappedBy = "techStack")
    private List<ProjectTechStack> projectTechStacks = new ArrayList<>();

    @OneToMany(mappedBy = "techStack")
    private List<MemberTechStack> memberTechStacks = new ArrayList<>();

    @OneToMany(mappedBy = "techStack")
    private List<PostTechStack> postTechStacks = new ArrayList<>();

}
