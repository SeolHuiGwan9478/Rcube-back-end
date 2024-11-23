package hufs.ces.rcube.domain.post.controller;

import hufs.ces.rcube.domain.post.dto.EventRequestDto;
import hufs.ces.rcube.domain.post.dto.EventResponseDto;
import hufs.ces.rcube.domain.post.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/posts")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    // 게시글 생성
    @PostMapping
    public ResponseEntity<EventResponseDto> createPost(@RequestBody EventRequestDto eventRequestDto) {
        EventResponseDto createdPost = eventService.saveEvent(eventRequestDto);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    // 모든 게시글 조회
    @GetMapping
    public ResponseEntity<Page<EventResponseDto>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));
        Page<EventResponseDto> postPage = eventService.getPosts(pageable);
        return new ResponseEntity<>(postPage, HttpStatus.OK);
    }

    // ID로 게시글 조회
    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDto> getPostById(@PathVariable Long id) {
        EventResponseDto post = eventService.getEventById(id);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    // 제목으로 게시글 조회
    @GetMapping("/title")
    public ResponseEntity<EventResponseDto> getPostByTitle(@RequestParam("title") String title) {
        EventResponseDto post = eventService.getEventByTitle(title);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    // 작성자 이름으로 게시글 페이징 조회
    @GetMapping("/author")
    public ResponseEntity<Page<EventResponseDto>> getPostsByAuthorPaged(
            @RequestParam("author") String authorName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Page<EventResponseDto> postPage = eventService.getEventsByAuthor(authorName, page, size, sortBy);
        return new ResponseEntity<>(postPage, HttpStatus.OK);
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<EventResponseDto> updatePost(@PathVariable Long id, @RequestBody EventRequestDto eventRequestDto) {
        EventResponseDto post = eventService.updateEvent(id, eventRequestDto);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    // ID로 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePostById(@PathVariable Long id) {
        eventService.deleteEventById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // 작성자 이름으로 게시글 삭제
    @DeleteMapping("/author")
    public ResponseEntity<Void> deletePostByAuthorName(@RequestBody EventRequestDto eventRequestDto) {
        eventService.deleteEventByAuthorName(eventRequestDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
