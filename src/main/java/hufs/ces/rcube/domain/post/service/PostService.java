package hufs.ces.rcube.domain.post.service;

import hufs.ces.rcube.domain.member.entity.Member;
import hufs.ces.rcube.domain.member.repository.MemberRepository;
import hufs.ces.rcube.domain.post.dto.MainPageDto;
import hufs.ces.rcube.domain.post.dto.PostRequestDto;
import hufs.ces.rcube.domain.post.dto.PostResponseDto;
import hufs.ces.rcube.domain.post.entity.*;
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
import java.util.*;


@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final EventRepository eventRepository;
    private final ProjectRepository projectRepository;

    public MainPageDto getMainPageData() {
        List<Post> posts = postRepository.findAll();
        Map<String, List<Curriculum>> curriculumsByMonth = new HashMap<>();

        // Set을 사용하여 중복된 프로젝트를 방지
        Set<Project> uniqueProjects = new HashSet<>();

        for (Post post : posts) {
            // 포스트의 프로젝트가 null이 아닐 경우에만 추가
            if (post.getProject() != null) {
                uniqueProjects.add(post.getProject());
            }

            // 커리큘럼을 달별로 그룹화
            for (Curriculum curriculum : post.getCurriculumList()) {
                curriculumsByMonth
                        .computeIfAbsent(curriculum.getMonth(), k -> new ArrayList<>())
                        .add(curriculum);
            }
        }

        int totalProjects = uniqueProjects.size(); // 고유 프로젝트의 개수 세기
        long totalMembers = memberRepository.count(); // 누적 학회원 수
        long currentMembers = memberRepository.count(); // 현재 학회원 수

        return MainPageDto.builder()
                .totalProjects(totalProjects)
                .curriculumsByMonth(curriculumsByMonth) // 달별 커리큘럼
                .totalMembers(totalMembers)
                .currentMembers(currentMembers)
                .build();
    }





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
        List<TechStack> techStacks = postRequestDto.getTechStack() != null
                ? postRequestDto.getTechStack()
                : new ArrayList<>(); // 혹은 null이 아닌 비어있는 리스트로 초기화

        Post post = Post.builder()
                .title(postRequestDto.getTitle())
                .content(postRequestDto.getContent())
                .author(author)
                .event(event)
                .project(project)
                .techStacks(techStacks)
                .build();

        Post savedPost = postRepository.save(post);
        return  PostResponseDto.convertToPostResponseDto(savedPost, "Post created successfully", HttpStatus.CREATED);
    }

    public PostResponseDto getPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Post not found with id: " + id));
        return  PostResponseDto.convertToPostResponseDto(post, "Post retrieved successfully", HttpStatus.OK);
    }

    public PostResponseDto getPostByTitle(String title) {
        Post post = postRepository.findByTitle(title).orElseThrow(() ->
                new IllegalArgumentException("Post not found with title: " + title));
        return  PostResponseDto.convertToPostResponseDto(post, "Post retrieved successfully", HttpStatus.OK);
    }

    public Page<PostResponseDto> getPostsByAuthor(String authorName, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));
        Page<Post> postPage = postRepository.findByAuthor(authorName, pageable);
        return postPage.map(post ->  PostResponseDto.convertToPostResponseDto(post, "Post retrieved successfully", HttpStatus.OK));
    }

    public Page<PostResponseDto> getPostsByEvent(Long eventId, Pageable pageable) {
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null) {
            throw new IllegalArgumentException("Event not found with id: " + eventId);
        }
        Page<Post> postPage = postRepository.findByEvent(event, pageable);
        return postPage.map(post ->  PostResponseDto.convertToPostResponseDto(post, "Post retrieved successfully", HttpStatus.OK));
    }

    public Page<PostResponseDto> getPostsByProject(Long projectId, Pageable pageable) {
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project == null) {
            throw new IllegalArgumentException("Project not found with id: " + projectId);
        }
        Page<Post> postPage = postRepository.findByProject(project, pageable);
        return postPage.map(post ->  PostResponseDto.convertToPostResponseDto(post, "Post retrieved successfully", HttpStatus.OK));
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
        return PostResponseDto.convertToPostResponseDto(savedPost, "Post updated successfully", HttpStatus.OK);
    }

    @Transactional
    public void deletePostById(Long id) {
        if (!postRepository.existsById(id)) {
            throw new IllegalArgumentException("Post not found with id: " + id);
        }
        postRepository.deleteById(id);
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
        return postPage.map(post -> PostResponseDto.convertToPostResponseDto(post, "Post retrieved successfully", HttpStatus.OK));
    }


}
