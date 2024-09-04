package hufs.ces.rcube.domain.post.dto;
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
    private Long eventId;
    private Long projectId;
    private String category; //공지사항, 프로젝트 관련,스터디 관련 등등
    private String eventDate; //이벤트 날짜
    private String location; //이벤트나 모임 장소
    private String attachmentUrl;
    //private List<String> tags;
    private String visibility; //공개 범위
    private List<String> tags;



}
