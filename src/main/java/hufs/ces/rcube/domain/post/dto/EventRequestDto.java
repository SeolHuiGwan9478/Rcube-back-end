package hufs.ces.rcube.domain.post.dto;
import lombok.*;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestDto {
    private Long id;
    private String title;
    private String content;
    private String author;





}
