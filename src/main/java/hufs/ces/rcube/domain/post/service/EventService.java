package hufs.ces.rcube.domain.post.service;

import hufs.ces.rcube.domain.member.entity.Member;
import hufs.ces.rcube.domain.member.repository.MemberRepository;
import hufs.ces.rcube.domain.post.dto.EventRequestDto;
import hufs.ces.rcube.domain.post.dto.EventResponseDto;
import hufs.ces.rcube.domain.post.entity.*;
import hufs.ces.rcube.domain.post.repository.EventRepository;
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
public class EventService {
    private final MemberRepository memberRepository;
    private final EventRepository eventRepository;







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
        Event post = eventRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Post not found with id: " + id));
        return  EventResponseDto.convertToEventResponseDto(post, "Post retrieved successfully", HttpStatus.OK);
    }

    public EventResponseDto getPostByTitle(String title) {
        Event post = eventRepository.findByTitle(title).orElseThrow(() ->
                new IllegalArgumentException("Post not found with title: " + title));
        return  EventResponseDto.convertToEventResponseDto(post, "Post retrieved successfully", HttpStatus.OK);
    }

    public Page<EventResponseDto> getPostsByAuthor(String authorName, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));
        Page<Event> postPage = eventRepository.findByAuthor(authorName, pageable);
        return postPage.map(post ->  EventResponseDto.convertToEventResponseDto(post, "Post retrieved successfully", HttpStatus.OK));
    }


    @Transactional
    public EventResponseDto updatePost(Long id, EventRequestDto eventRequestDto) {
        Event existingPost = eventRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Post not found with id: " + id));

        Member author = memberRepository.findByAuthorName(eventRequestDto.getAuthor());
        if (author == null) {
            throw new IllegalArgumentException("Author not found");
        }


        Event updatedPost = Event.builder()
                .id(existingPost.getId())
                .title(eventRequestDto.getTitle())
                .content(eventRequestDto.getContent())
                .author(author)
                .createdAt(existingPost.getCreatedAt())
                .updatedAt(LocalDate.now())
                .build();

        Event savedPost = eventRepository.save(updatedPost);
        return EventResponseDto.convertToEventResponseDto(savedPost, "Post updated successfully", HttpStatus.OK);
    }

    @Transactional
    public void deletePostById(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new IllegalArgumentException("Post not found with id: " + id);
        }
        eventRepository.deleteById(id);
    }


    @Transactional
    public void deleteByAuthorName(EventRequestDto eventRequestDto) {
        String authorname = eventRequestDto.getAuthor();
        Member author = memberRepository.findByAuthorName(authorname);

        if (author != null) {
            eventRepository.deleteByAuthorName(authorname);
        } else {
            throw new IllegalArgumentException("Author not found with name: " + authorname);
        }
    }

    public Page<EventResponseDto> getPosts(Pageable pageable) {
        Page<Event> postPage = eventRepository.findAll(pageable);
        return postPage.map(post -> EventResponseDto.convertToEventResponseDto(post, "Post retrieved successfully", HttpStatus.OK));
    }


}
