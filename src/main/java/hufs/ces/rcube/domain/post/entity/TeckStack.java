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
public class TeckStack {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; //기술 스택 이름

    @OneToMany(mappedBy = "teckStack")
    private List<ProjectTechStack> projectTeckStacks = new ArrayList<>();

    @OneToMany(mappedBy = "teckStack")
    private List<MemberTeckStack> memberTeckStacks = new ArrayList<>();

    @OneToMany(mappedBy = "teckStack")
    private List<PostTeckStack> postTeckStacks = new ArrayList<>();

}
