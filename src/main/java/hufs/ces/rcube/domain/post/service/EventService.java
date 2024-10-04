package hufs.ces.rcube.domain.post.service;

import hufs.ces.rcube.domain.member.entity.Member;
import hufs.ces.rcube.domain.member.repository.MemberRepository;
import hufs.ces.rcube.domain.post.dto.MainPageDto;
import hufs.ces.rcube.domain.post.dto.EventRequestDto;
import hufs.ces.rcube.domain.post.dto.EventResponseDto;
import hufs.ces.rcube.domain.post.entity.*;
import hufs.ces.rcube.domain.post.repository.EventRepository;
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
public class EventService {
    private final MemberRepository memberRepository;
    private final EventRepository eventRepository;
    private final ProjectRepository projectRepository;






    @Transactional
    public EventResponseDto savePost(EventRequestDto eventRequestDto) { // create
        Member author = memberRepository.findByAuthorName(eventRequestDto.getAuthor());
        if (author == null) {
            throw new IllegalArgumentException("Author not found");
        }
        Event post = Event.builder()
                .title(eventRequestDto.getTitle())
                .content(eventRequestDto.getContent())
                .author(author)
                .build();

        Event savedPost = eventRepository.save(post);
        return  EventResponseDto.convertToEventResponseDto(savedPost, "Post created successfully", HttpStatus.CREATED);
    }

    public EventResponseDto getPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Post not found with id: " + id));
        return  EventResponseDto.convertToPostResponseDto(post, "Post retrieved successfully", HttpStatus.OK);
    }

    public EventResponseDto getPostByTitle(String title) {
        Post post = postRepository.findByTitle(title).orElseThrow(() ->
                new IllegalArgumentException("Post not found with title: " + title));
        return  EventResponseDto.convertToPostResponseDto(post, "Post retrieved successfully", HttpStatus.OK);
    }

    public Page<EventResponseDto> getPostsByAuthor(String authorName, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));
        Page<Post> postPage = postRepository.findByAuthor(authorName, pageable);
        return postPage.map(post ->  EventResponseDto.convertToPostResponseDto(post, "Post retrieved successfully", HttpStatus.OK));
    }

    public Page<EventResponseDto> getPostsByEvent(Long eventId, Pageable pageable) {
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null) {
            throw new IllegalArgumentException("Event not found with id: " + eventId);
        }
        Page<Post> postPage = postRepository.findByEvent(event, pageable);
        return postPage.map(post ->  EventResponseDto.convertToPostResponseDto(post, "Post retrieved successfully", HttpStatus.OK));
    }

    public Page<EventResponseDto> getPostsByProject(Long projectId, Pageable pageable) {
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project == null) {
            throw new IllegalArgumentException("Project not found with id: " + projectId);
        }
        Page<Post> postPage = postRepository.findByProject(project, pageable);
        return postPage.map(post ->  EventResponseDto.convertToPostResponseDto(post, "Post retrieved successfully", HttpStatus.OK));
    }

    @Transactional
    public EventResponseDto updatePost(Long id, EventRequestDto eventRequestDto) {
        Post existingPost = postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Post not found with id: " + id));

        Member author = memberRepository.findByAuthorName(eventRequestDto.getAuthor());
        if (author == null) {
            throw new IllegalArgumentException("Author not found");
        }

        Event event = eventRequestDto.getEventId() != null
                ? eventRepository.findById(eventRequestDto.getEventId()).orElse(null)
                : null;
        Project project = eventRequestDto.getProjectId() != null
                ? projectRepository.findById(eventRequestDto.getProjectId()).orElse(null)
                : null;

        Post updatedPost = Post.builder()
                .id(existingPost.getId())
                .title(eventRequestDto.getTitle())
                .content(eventRequestDto.getContent())
                .author(author)
                .event(event)
                .project(project)
                .createdAt(existingPost.getCreatedAt())
                .updatedAt(LocalDate.now())
                .build();

        Post savedPost = postRepository.save(updatedPost);
        return EventResponseDto.convertToPostResponseDto(savedPost, "Post updated successfully", HttpStatus.OK);
    }

    @Transactional
    public void deletePostById(Long id) {
        if (!postRepository.existsById(id)) {
            throw new IllegalArgumentException("Post not found with id: " + id);
        }
        postRepository.deleteById(id);
    }


    @Transactional
    public void deleteByAuthorName(EventRequestDto eventRequestDto) {
        String authorname = eventRequestDto.getAuthor();
        Member author = memberRepository.findByAuthorName(authorname);

        if (author != null) {
            postRepository.deleteByAuthorName(authorname);
        } else {
            throw new IllegalArgumentException("Author not found with name: " + authorname);
        }
    }

    public Page<EventResponseDto> getPosts(Pageable pageable) {
        Page<Post> postPage = postRepository.findAll(pageable);
        return postPage.map(post -> EventResponseDto.convertToPostResponseDto(post, "Post retrieved successfully", HttpStatus.OK));
    }


}
