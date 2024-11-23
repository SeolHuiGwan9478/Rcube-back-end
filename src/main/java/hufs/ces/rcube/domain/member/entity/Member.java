package hufs.ces.rcube.domain.member.entity;

import hufs.ces.rcube.domain.post.entity.Event;
import hufs.ces.rcube.domain.post.entity.MemberProject;
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
    private String email; //연락망
    private String oauthId;
    private String imageUrl;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String name;
    private String password;
    // extra info
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "member")
    private List<MemberProject> memberProjects = new ArrayList<>();
//    public Member(String oauthId, String name, String email, String imageUrl, Role role) {
//        this(null, oauthId, name, email, imageUrl, role);
//    }



    public Member(Long id, String oauthId, String name, String email, String imageUrl, Role role) {
        this.id = id;
        this.oauthId = oauthId;
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.role = role;
    }

    public Member update(String name, String email, String imageUrl) {
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        return this;
    }



}
