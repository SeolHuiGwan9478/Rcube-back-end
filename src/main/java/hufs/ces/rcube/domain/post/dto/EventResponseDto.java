package hufs.ces.rcube.domain.post.dto;

import hufs.ces.rcube.domain.post.entity.Event;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

@Getter
@Builder
public class EventResponseDto {
    private String message; // 응답 메시지
    private EventData data; // EventData 객체

    @Getter
    @Builder
    public static class EventData {
        private Long id;                // 게시글 ID
        private String title;           // 게시글 제목
        private String content;         // 게시글 내용
        private String author;          // 작성자 이름
        private LocalDate createdAt;    // 생성 시간
        private LocalDate updatedAt;    // 수정 시간
    }

    public static EventResponseDto convertToEventResponseDto(Event event, String message, HttpStatus status) {
        EventData eventData = EventData.builder()
                .id(event.getId())
                .title(event.getTitle())
                .content(event.getContent())
                .author(event.getAuthor().getName())  // Member 객체에서 이름 추출
                .createdAt(event.getCreatedAt())
                .updatedAt(event.getUpdatedAt())
                .build();

        return EventResponseDto.builder()
                .data(eventData)  // eventData 객체 사용
                .message(message)
                .build();
    }
}
