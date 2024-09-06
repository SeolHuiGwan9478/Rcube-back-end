package hufs.ces.rcube.domain.post.controller;

import hufs.ces.rcube.domain.post.dto.PostRequestDto;
import hufs.ces.rcube.domain.post.dto.PostResponseDto;
import hufs.ces.rcube.domain.post.service.PostService;
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
public class PostController {
    private final PostService postService;

    // 게시글 생성
    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto postRequestDto) {
        try {
            PostResponseDto createdPost = postService.savePost(postRequestDto);
            return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // 모든 게시글 조회
    @GetMapping("/all")
    public ResponseEntity<Page<PostResponseDto>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));
        Page<PostResponseDto> postPage = postService.getPosts(pageable);
        return new ResponseEntity<>(postPage, HttpStatus.OK);
    }

    // ID로 게시글 조회
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long id) {
        try {
            PostResponseDto post = postService.getPostById(id);
            return new ResponseEntity<>(post, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 제목으로 게시글 조회
    @GetMapping("/title")
    public ResponseEntity<PostResponseDto> getPostByTitle(@RequestParam String title) {
        try {
            PostResponseDto post = postService.getPostByTitle(title);
            return new ResponseEntity<>(post, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 작성자 이름으로 게시글 페이징 조회
    @GetMapping("/author/{authorName}/paged")
    public ResponseEntity<Page<PostResponseDto>> getPostsByAuthorPaged(
            @PathVariable String authorName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Page<PostResponseDto> postPage = postService.getPostsByAuthor(authorName, page, size, sortBy);
        return new ResponseEntity<>(postPage, HttpStatus.OK);
    }

    // 이벤트로 게시글 페이징 조회
    @GetMapping("/event/{eventId}")
    public ResponseEntity<Page<PostResponseDto>> getPostsByEvent(@PathVariable Long eventId, Pageable pageable) {
        Page<PostResponseDto> postPage = postService.getPostsByEvent(eventId, pageable);
        return new ResponseEntity<>(postPage, HttpStatus.OK);
    }

    // 프로젝트로 게시글 페이징 조회
    @GetMapping("/project/{projectId}")
    public ResponseEntity<Page<PostResponseDto>> getPostsByProject(@PathVariable Long projectId, Pageable pageable) {
        Page<PostResponseDto> postPage = postService.getPostsByProject(projectId, pageable);
        return new ResponseEntity<>(postPage, HttpStatus.OK);
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long id, @RequestBody PostRequestDto postRequestDto) {
        try {
            PostResponseDto post = postService.updatePost(id, postRequestDto);
            return new ResponseEntity<>(post, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // ID로 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePostById(@PathVariable Long id) {
        try {
            postService.deletePostById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 제목으로 게시글 삭제
    @DeleteMapping("/title")
    public ResponseEntity<Void> deletePostByTitle(@RequestParam String title) {
        try {
            postService.deleteByTitle(title);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/author")
    public ResponseEntity<Void> deletePostByAuthorName(@RequestBody PostRequestDto postRequestDto) {
        try {
            postService.deleteByAuthorName(postRequestDto);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
