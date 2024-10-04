package hufs.ces.rcube.domain.post.entity;

import hufs.ces.rcube.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    @CreatedDate
    private LocalDate createdAt;
    @LastModifiedDate
    private LocalDate updatedAt;


    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member author; //이벤트에 관한 게시글을 쓴 작성자



}
