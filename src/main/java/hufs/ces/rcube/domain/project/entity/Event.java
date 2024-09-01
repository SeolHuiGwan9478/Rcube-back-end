package hufs.ces.rcube.domain.project.entity;

import hufs.ces.rcube.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private LocalDateTime eventDate;
    private String location;
    @OneToMany(mappedBy = "event")
    private List<MemberEvent> memberEvents; //이벤트에 참여한 사람들


    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member organizer; //이벤트를 연 주최자

    @OneToMany(mappedBy = "event")
    private List<Post> posts = new ArrayList<>(); // 이벤트와 연관된 게시글

    @ManyToOne()
    @JoinColumn(name = "PROJECT_ID")
    private Project project; //어떤 프로젝트에 대한 이벤트

}
