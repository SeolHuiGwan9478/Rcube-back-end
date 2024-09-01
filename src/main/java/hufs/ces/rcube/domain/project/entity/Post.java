package hufs.ces.rcube.domain.project.entity;

import hufs.ces.rcube.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Post {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long title;
    private Long content;
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID") //게시글을 쓴 작성자
    private Member member;

    @OneToOne(mappedBy = "post")
    private Project project; // 하나의 프로젝트는 하나의 게시글

    @ManyToOne
    @JoinColumn(name = "EVENT_ID") // 선택적으로 연관될 수 있음
    private Event event;

    @CreatedDate
    private LocalDate createdAt;
    @LastModifiedDate
    private LocalDate updatedAt;





}
