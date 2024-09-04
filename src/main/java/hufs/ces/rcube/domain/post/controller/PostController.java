package hufs.ces.rcube.domain.post.controller;

import hufs.ces.rcube.domain.post.dto.PostRequestDto;
import hufs.ces.rcube.domain.post.dto.PostResponseDto;
import hufs.ces.rcube.domain.post.entity.Post;
import hufs.ces.rcube.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    // 게시글 생성
    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostRequestDto postRequestDto) {
        try {
            Post createdPost = postService.savePost(postRequestDto);
            return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    // 모든 게시글 조회
    @GetMapping("/all")
    public ResponseEntity<List<PostResponseDto>> getAllPosts() {
        List<Post> postList = postService.getPostList();
        return new ResponseEntity<>(postList, HttpStatus.OK);
    }

    // ID로 게시글 조회
    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        Post post = postService.getPostById(id);
        if (post != null) {
            return new ResponseEntity<>(post, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 제목으로 게시글 조회
    @GetMapping("/title")
    public ResponseEntity<Post> getPostByTitle(@RequestParam String title) {
        Post post = postService.getPostByTitle(title);
        if (post != null) {
            return new ResponseEntity<>(post, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    // 작성자 이름으로 게시글 페이징 조회
    @GetMapping("/author/{authorName}/paged")
    public ResponseEntity<Page<Post>> getPostsByAuthorPaged(
            @PathVariable String authorName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Page<Post> posts = postService.getPostsByAuthor(authorName, page, size, sortBy);
        return ResponseEntity.ok(posts);
    }
    //이벤트로 게시글 페이징 조회
    @GetMapping("/event/{eventId}")
    public Page<Post> getPostsByEvent(@PathVariable Long eventId, Pageable pageable) {
        return postService.getPostsByEvent(eventId, pageable);
    }
    //프로젝트로 게시글 페이징 조회
    @GetMapping("/project/{projectId}")
    public Page<Post> getPostsByProject(@PathVariable Long projectId, Pageable pageable) {
        return postService.getPostsByProject(projectId, pageable);
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody PostRequestDto postRequestDto) {
        Post post = postService.updatePost(id, postRequestDto);
        if (post != null) {
            return new ResponseEntity<>(post, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // ID로 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePostById(@PathVariable Long id) {
        try {
            postService.deletePostById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 제목으로 게시글 삭제
    @DeleteMapping("/title")
    public ResponseEntity<Void> deletePostByTitle(@RequestParam String title) {
        try {
            postService.deleteByTitle(title);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/author")
    public ResponseEntity<Void> deletePostByAuthorName(@RequestBody PostRequestDto postRequestDto){
        try {
            postService.deleteByAuthorName(postRequestDto);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public Page<Post> getPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));
        return postService.getPosts(pageable);
    }
    // 날짜 범위로 게시글 조회
    @GetMapping("/date-range")
    public Page<PostResponseDto> getPostsByDateRange(
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate,
            Pageable pageable) {

        // 게시글 조회 서비스 호출
        Page<PostResponseDto> posts = postService.getPostsByDateRange(startDate, endDate, pageable)
                .map(PostResponseDto::new);

        return posts;
    }
}