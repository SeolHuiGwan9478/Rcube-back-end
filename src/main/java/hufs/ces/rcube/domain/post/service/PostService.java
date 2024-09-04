package hufs.ces.rcube.domain.post.service;

import hufs.ces.rcube.domain.member.entity.Member;
import hufs.ces.rcube.domain.member.repository.MemberRepository;
import hufs.ces.rcube.domain.post.dto.PostRequestDto;
import hufs.ces.rcube.domain.post.entity.Event;
import hufs.ces.rcube.domain.post.entity.Post;
import hufs.ces.rcube.domain.post.entity.Project;
import hufs.ces.rcube.domain.post.repository.EventRepository;
import hufs.ces.rcube.domain.post.repository.PostRepository;
import hufs.ces.rcube.domain.post.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final EventRepository eventRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public Post savePost(PostRequestDto postRequestDto){ //create
        Member author = memberRepository.findByAuthorName(postRequestDto.getAuthor());
        if(author == null){
            throw new IllegalArgumentException("Author not found");
        }

        // 이벤트와 프로젝트는 선택적일 수 있음
        Event event = postRequestDto.getEventId() != null
                ? eventRepository.findById(postRequestDto.getEventId()).orElse(null)
                : null;
        Project project = postRequestDto.getProjectId() != null
                ? projectRepository.findById(postRequestDto.getProjectId()).orElse(null)
                : null;

        Post post = Post.builder()
                .title(postRequestDto.getTitle())
                .content(postRequestDto.getContent())
                .author(author)
                .event(event)
                .project(project)
                .build();

        return postRepository.save(post);
    }

    public List<Post> getPostList() {
        List<Post> posts = postRepository.findAll();
        if (posts.isEmpty()) {
            throw new NoSuchElementException("No posts found");
        }
        return posts;
    }


    public Post getPostById(Long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Post not found with id: " + id));
    }
    // 작성자 이름으로 게시글 페이징 조회
    public Page<Post> getPostsByAuthor(String authorName, int page, int size, String sortBy) {
        // Pageable 객체 생성 (정렬 기준과 방향 지정)
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));


        return postRepository.findByAuthor(authorName, pageable);
    }



    public Post getPostByTitle(String title) { //제목으로 조회
        return postRepository.findByTitle(title).orElseThrow(() ->
                new IllegalArgumentException("Post not found with title: " + title));
    }


    // 특정 이벤트로 게시글 조회
    public Page<Post> getPostsByEvent(Long eventId, Pageable pageable) {
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null) {
            throw new IllegalArgumentException("Event not found with id: " + eventId);
        }
        return postRepository.findByEvent(event, pageable);
    }

    // 특정 프로젝트로 게시글 조회
    public Page<Post> getPostsByProject(Long projectId, Pageable pageable) {
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project == null) {
            throw new IllegalArgumentException("Project not found with id: " + projectId);
        }
        return postRepository.findByProject(project, pageable);
    }

    @Transactional
    public Post updatePost(Long id, PostRequestDto postRequestDto) {
        // ID를 기반으로 기존 게시글을 찾음
        Post existingPost = postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Post not found with id: " + id));

        // 작성자 업데이트 (존재하는 작성자인지 확인)
        Member author = memberRepository.findByAuthorName(postRequestDto.getAuthor());
        if (author == null) {
            throw new IllegalArgumentException("Author not found");
        }

        // 이벤트와 프로젝트는 선택적이므로 null 체크
        Event event = postRequestDto.getEventId() != null
                ? eventRepository.findById(postRequestDto.getEventId()).orElse(null)
                : null;
        Project project = postRequestDto.getProjectId() != null
                ? projectRepository.findById(postRequestDto.getProjectId()).orElse(null)
                : null;

        Post updatedPost = Post.builder()
                .id(existingPost.getId())  // 기존 ID 유지
                .title(postRequestDto.getTitle())
                .content(postRequestDto.getContent())
                .author(author)            // 작성자 업데이트
                .event(event)              // 이벤트 업데이트
                .project(project)          // 프로젝트 업데이트
                .createdAt(existingPost.getCreatedAt()) // 기존 생성일자 유지
                .updatedAt(LocalDate.now())            // 업데이트 시간 갱신
                .build();


        // 수정된 게시글 저장
        return postRepository.save(updatedPost);
    }

    @Transactional
    public void deletePostById(Long id) {
        if (!postRepository.existsById(id)) {
            throw new IllegalArgumentException("Post not found with id: " + id);
        }
        postRepository.deleteById(id);
    }

    @Transactional
    public void deleteByTitle(String title) {
        if (!postRepository.existsByTitle(title)) {
            throw new IllegalArgumentException("Post not found with title: " + title);
        }
        postRepository.deleteByTitle(title);
    }

    @Transactional
    //delete(작성자이름으로)
    public void deleteByAuthorName(PostRequestDto postRequestDto) { //delete(작성자로)
        String authorname = postRequestDto.getAuthor();
        Member author = memberRepository.findByAuthorName(authorname);

        // 작성자가 존재하는지
        if (author != null) {
            // 작성자 이름으로 게시글 삭제
            postRepository.deleteByAuthorName(authorname);
        } else {
            // 작성자가 없는 경우 예외 처리
            throw new IllegalArgumentException("Post not found with title:" + authorname);
        }


    }
    //페이징 및 정렬 메서드
    public Page<Post> getPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }
    // 작성일자 범위로 게시글 조회
    public Page<Post> getPostsByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return postRepository.findByCreatedAtBetween(startDate, endDate, pageable);
    }


}
