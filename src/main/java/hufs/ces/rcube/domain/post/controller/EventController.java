package hufs.ces.rcube.domain.post.controller;

import hufs.ces.rcube.domain.post.dto.MainPageDto;
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
    // 메인 페이지 데이터 API
    @GetMapping("/main")
    public MainPageDto getMainPageData() {
        return eventService.getMainPageData();
    }

    // 게시글 생성
    @PostMapping
    public ResponseEntity<EventResponseDto> createPost(@RequestBody EventRequestDto eventRequestDto) {
        try {
            EventResponseDto createdPost = eventService.savePost(eventRequestDto);
            return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
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
        try {
            EventResponseDto post = eventService.getPostById(id);
            return new ResponseEntity<>(post, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 제목으로 게시글 조회
    @GetMapping
    public ResponseEntity<EventResponseDto> getPostByTitle(@RequestParam("title") String title) {
        try {
            EventResponseDto post = eventService.getPostByTitle(title);
            return new ResponseEntity<>(post, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 작성자 이름으로 게시글 페이징 조회
    @GetMapping
    public ResponseEntity<Page<EventResponseDto>> getPostsByAuthorPaged(
            @RequestParam("author") String authorName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Page<EventResponseDto> postPage = eventService.getPostsByAuthor(authorName, page, size, sortBy);
        return new ResponseEntity<>(postPage, HttpStatus.OK);
    }

    // 이벤트로 게시글 페이징 조회
    @GetMapping
    public ResponseEntity<Page<EventResponseDto>> getPostsByEvent(@RequestParam("event") Long eventId, Pageable pageable) {
        Page<EventResponseDto> postPage = eventService.getPostsByEvent(eventId, pageable);
        return new ResponseEntity<>(postPage, HttpStatus.OK);
    }

    // 프로젝트로 게시글 페이징 조회
    @GetMapping
    public ResponseEntity<Page<EventResponseDto>> getPostsByProject(@RequestParam("project") Long projectId, Pageable pageable) {
        Page<EventResponseDto> postPage = eventService.getPostsByProject(projectId, pageable);
        return new ResponseEntity<>(postPage, HttpStatus.OK);
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<EventResponseDto> updatePost(@PathVariable Long id, @RequestBody EventRequestDto eventRequestDto) {
        try {
            EventResponseDto post = eventService.updatePost(id, eventRequestDto);
            return new ResponseEntity<>(post, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // ID로 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePostById(@PathVariable Long id) {
        try {
            eventService.deletePostById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping
    public ResponseEntity<Void> deletePostByAuthorName(@RequestBody EventRequestDto eventRequestDto) {
        try {
            eventService.deleteByAuthorName(eventRequestDto);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
