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
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String projectName;
    private String description;
    private int year; //프로젝트 연도
    @CreatedDate
    private LocalDate createdAt;
    @LastModifiedDate
    private LocalDate updatedAt;
    private String imageUrl;
    private String projectLink;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")// MEMBER_ID를 외래 키로 사용
    private Member author; //이벤트에 관한 게시글을 쓴 작성자



    @OneToMany(mappedBy = "project")
    private List<MemberProject> memberProjects = new ArrayList<>();





}
