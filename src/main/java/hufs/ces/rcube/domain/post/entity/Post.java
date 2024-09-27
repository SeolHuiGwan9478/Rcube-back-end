package hufs.ces.rcube.domain.post.entity;

import hufs.ces.rcube.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Post {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private String category;
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID") //게시글을 쓴 작성자
    private Member author;

    @ManyToOne
    @JoinColumn(name = "PROJECT_ID") // 선택적: 프로젝트와 연결될 수 있음
    private Project project;

    @ManyToOne
    @JoinColumn(name = "EVENT_ID") // 선택적으로 연관될 수 있음
    private Event event;

    @OneToMany(mappedBy = "post")
    private List<TechStack> techStacks = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Curriculum> curriculumList = new ArrayList<>();

    @CreatedDate
    private LocalDate createdAt;
    @LastModifiedDate
    private LocalDate updatedAt;





}
