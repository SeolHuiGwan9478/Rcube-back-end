package hufs.ces.rcube.domain.post.service;

import hufs.ces.rcube.domain.member.entity.Member;
import hufs.ces.rcube.domain.member.repository.MemberRepository;
import hufs.ces.rcube.domain.post.dto.PostRequestDto;
import hufs.ces.rcube.domain.post.dto.PostResponseDto;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final EventRepository eventRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public PostResponseDto savePost(PostRequestDto postRequestDto) { // create
        Member author = memberRepository.findByAuthorName(postRequestDto.getAuthor());
        if (author == null) {
            throw new IllegalArgumentException("Author not found");
        }

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

        Post savedPost = postRepository.save(post);
        return convertToPostResponseDto(savedPost, "Post created successfully", HttpStatus.CREATED);
    }

    public PostResponseDto getPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Post not found with id: " + id));
        return convertToPostResponseDto(post, "Post retrieved successfully", HttpStatus.OK);
    }

    public PostResponseDto getPostByTitle(String title) {
        Post post = postRepository.findByTitle(title).orElseThrow(() ->
                new IllegalArgumentException("Post not found with title: " + title));
        return convertToPostResponseDto(post, "Post retrieved successfully", HttpStatus.OK);
    }

    public Page<PostResponseDto> getPostsByAuthor(String authorName, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));
        Page<Post> postPage = postRepository.findByAuthor(authorName, pageable);
        return postPage.map(post -> convertToPostResponseDto(post, "Post retrieved successfully", HttpStatus.OK));
    }

    public Page<PostResponseDto> getPostsByEvent(Long eventId, Pageable pageable) {
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null) {
            throw new IllegalArgumentException("Event not found with id: " + eventId);
        }
        Page<Post> postPage = postRepository.findByEvent(event, pageable);
        return postPage.map(post -> convertToPostResponseDto(post, "Post retrieved successfully", HttpStatus.OK));
    }

    public Page<PostResponseDto> getPostsByProject(Long projectId, Pageable pageable) {
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project == null) {
            throw new IllegalArgumentException("Project not found with id: " + projectId);
        }
        Page<Post> postPage = postRepository.findByProject(project, pageable);
        return postPage.map(post -> convertToPostResponseDto(post, "Post retrieved successfully", HttpStatus.OK));
    }

    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto postRequestDto) {
        Post existingPost = postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Post not found with id: " + id));

        Member author = memberRepository.findByAuthorName(postRequestDto.getAuthor());
        if (author == null) {
            throw new IllegalArgumentException("Author not found");
        }

        Event event = postRequestDto.getEventId() != null
                ? eventRepository.findById(postRequestDto.getEventId()).orElse(null)
                : null;
        Project project = postRequestDto.getProjectId() != null
                ? projectRepository.findById(postRequestDto.getProjectId()).orElse(null)
                : null;

        Post updatedPost = Post.builder()
                .id(existingPost.getId())
                .title(postRequestDto.getTitle())
                .content(postRequestDto.getContent())
                .author(author)
                .event(event)
                .project(project)
                .createdAt(existingPost.getCreatedAt())
                .updatedAt(LocalDate.now())
                .build();

        Post savedPost = postRepository.save(updatedPost);
        return convertToPostResponseDto(savedPost, "Post updated successfully", HttpStatus.OK);
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
    public void deleteByAuthorName(PostRequestDto postRequestDto) {
        String authorname = postRequestDto.getAuthor();
        Member author = memberRepository.findByAuthorName(authorname);

        if (author != null) {
            postRepository.deleteByAuthorName(authorname);
        } else {
            throw new IllegalArgumentException("Author not found with name: " + authorname);
        }
    }

    public Page<PostResponseDto> getPosts(Pageable pageable) {
        Page<Post> postPage = postRepository.findAll(pageable);
        return postPage.map(post -> convertToPostResponseDto(post, "Post retrieved successfully", HttpStatus.OK));
    }

    private PostResponseDto convertToPostResponseDto(Post post, String message, HttpStatus status) {
        PostResponseDto.PostData postData = PostResponseDto.PostData.builder()
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
