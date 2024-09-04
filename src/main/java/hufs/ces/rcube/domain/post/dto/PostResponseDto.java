package hufs.ces.rcube.domain.post.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {
    private int statusCode; //HTTP상태 코드
    private String message; //응답 메시지
    private PostData data;



    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostData {
        private Long id;                // 게시글 ID
        private String title;           // 게시글 제목
        private String content;         // 게시글 내용
        private String author;          // 작성자 이름
        private String category;        // 카테고리
        private LocalDateTime createdAt; // 생성 시간
        private LocalDateTime updatedAt; // 수정 시간
    }


}
