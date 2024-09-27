package hufs.ces.rcube.domain.post.dto;
import hufs.ces.rcube.domain.post.entity.TechStack;
import lombok.*;

import java.util.List;
@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDto {
    private Long id;
    private String title;
    private String content;
    private String author;
    private Long eventId; //이벤트 게시글에만 해당
    private Long projectId; //프로젝트 게시글에만 해당
    private List<TechStack> techStack; // 프로젝트 게시글, 학회원 소개 게시글일 경우에 필
    private String category; //공지사항, 프로젝트 관련,스터디 관련 등등
    private String eventDate; //이벤트 날짜, 이벤트 게시글일 경우 필요
    private String location; //이벤트 게시글일 경우 필요

    //private List<String> tags;





}
