package hufs.ces.rcube.domain.member.entity;

import hufs.ces.rcube.domain.post.entity.MemberEvent;
import hufs.ces.rcube.domain.post.entity.MemberProject;
import hufs.ces.rcube.domain.post.entity.Post;
import hufs.ces.rcube.domain.post.entity.Team;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String name;
    private String password;
    // extra info
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "member")
    private List<MemberProject> memberProjects = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<MemberEvent> memberEvents = new ArrayList<>(); //이벤트에 참여한 사람들

    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    @OneToMany(mappedBy = "author")
    private List<Post> posts = new ArrayList<>();



}
