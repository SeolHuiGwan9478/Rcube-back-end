package hufs.ces.rcube.domain.post.entity;

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
    private String name;
    private String description;
    private int year; //프로젝트 연도
    @CreatedDate
    private LocalDate createdAt;
    @LastModifiedDate
    private LocalDate updatedAt;
    private String imageUrl;
    private String projectLink;



    @OneToMany(mappedBy = "project")
    private List<MemberProject> memberProjects = new ArrayList<>();


    @OneToMany(mappedBy = "project")
    private List<Event> events = new ArrayList<>(); // 프로젝트는 여러 이벤트를 가짐


}
