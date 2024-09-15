package hufs.ces.rcube.domain.post.dto;

import hufs.ces.rcube.domain.post.entity.Post;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class PostResponseDto {
    private String message; //응답 메시지
    private PostData data;



    @Getter
    @Builder
    public static class PostData {
        private Long id;                // 게시글 ID
        private String title;           // 게시글 제목
        private String content;         // 게시글 내용
        private String author;          // 작성자 이름
        private String category;        // 카테고리
        private LocalDate createdAt; // 생성 시간
        private LocalDate updatedAt; // 수정 시간
    }

    public static PostResponseDto convertToPostResponseDto(Post post, String message, HttpStatus status) {
        PostData postData = PostData.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getAuthor().getName())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();

        return PostResponseDto.builder()
                .data(postData)
                .message(message)
                .build();
    }


}
