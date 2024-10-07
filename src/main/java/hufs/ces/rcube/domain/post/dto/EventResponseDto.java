package hufs.ces.rcube.domain.post.dto;

import hufs.ces.rcube.domain.post.entity.Event;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class EventResponseDto {
    private String message; // 응답 메시지
    private List<EventData> data; // 여러 EventData 객체를 담을 리스트로 변경

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

    // 단일 이벤트를 변환하는 메서드
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
                .data(List.of(eventData))  // 단일 이벤트 데이터를 리스트에 담음
                .message(message)
                .build();
    }

    // 다중 이벤트를 변환하는 메서드
    public static EventResponseDto convertToEventsResponseDto(List<Event> events, String message, HttpStatus status) {
        List<EventData> eventDataList = events.stream()
                .map(event -> EventData.builder()
                        .id(event.getId())
                        .title(event.getTitle())
                        .content(event.getContent())
                        .author(event.getAuthor().getName())  // Member 객체에서 이름 추출
                        .createdAt(event.getCreatedAt())
                        .updatedAt(event.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());

        return EventResponseDto.builder()
                .data(eventDataList)  // 리스트로 변환된 이벤트 데이터 사용
                .message(message)
                .build();
    }
}
