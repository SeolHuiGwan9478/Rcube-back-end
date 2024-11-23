package hufs.ces.rcube.domain.post.service;

import hufs.ces.rcube.domain.member.entity.Member;
import hufs.ces.rcube.domain.member.repository.MemberRepository;
import hufs.ces.rcube.domain.post.dto.EventRequestDto;
import hufs.ces.rcube.domain.post.dto.EventResponseDto;
import hufs.ces.rcube.domain.post.entity.Event;
import hufs.ces.rcube.domain.post.repository.EventRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class EventServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveEvent() {
        // Given
        Member member = Member.builder().name("author").build();
        when(memberRepository.findByName("author")).thenReturn(member);

        EventRequestDto eventRequestDto = EventRequestDto.builder()
                .title("Title")
                .content("Content")
                .author("author")
                .build();

        Event savedPost = Event.builder()
                .id(1L)
                .title("Title")
                .content("Content")
                .author(member)
                .createdAt(LocalDate.now())
                .build();

        when(eventRepository.save(any(Event.class))).thenReturn(savedPost);

        // When
        EventResponseDto response = eventService.saveEvent(eventRequestDto);

        // Then
        assertThat(response).isNotNull();
        EventResponseDto.EventData eventData = response.getData().get(0); // Access first element
        assertThat(eventData.getTitle()).isEqualTo("Title");
        assertThat(eventData.getContent()).isEqualTo("Content");
        assertThat(eventData.getAuthor()).isEqualTo("author");
        assertThat(response.getMessage()).isEqualTo("Post created successfully");
    }

    @Test
    void getEventById() {
        // Given
        Event post = Event.builder()
                .id(1L)
                .title("Title")
                .content("Content")
                .author(Member.builder().name("author").build())
                .createdAt(LocalDate.now())
                .build();

        when(eventRepository.findById(1L)).thenReturn(Optional.of(post));

        // When
        EventResponseDto response = eventService.getEventById(1L);

        // Then
        assertThat(response).isNotNull();
        EventResponseDto.EventData eventData = response.getData().get(0); // Access first element
        assertThat(eventData.getTitle()).isEqualTo("Title");
        assertThat(response.getMessage()).isEqualTo("Post retrieved successfully");
    }

    @Test
    void getEventByTitle() {
        // Given
        Event post = Event.builder()
                .id(1L)
                .title("Title")
                .content("Content")
                .author(Member.builder().name("author").build())
                .createdAt(LocalDate.now())
                .build();

        when(eventRepository.findByTitle("Title")).thenReturn(Optional.of(post));

        // When
        EventResponseDto response = eventService.getEventByTitle("Title");

        // Then
        assertThat(response).isNotNull();
        EventResponseDto.EventData eventData = response.getData().get(0); // Access first element
        assertThat(eventData.getTitle()).isEqualTo("Title");
        assertThat(response.getMessage()).isEqualTo("Post retrieved successfully");
    }

    @Test
    void getEventsByAuthor() {
        // Given
        Event post = Event.builder()
                .id(1L)
                .title("Title")
                .content("Content")
                .author(Member.builder().name("author").build())
                .createdAt(LocalDate.now())
                .build();

        Page<Event> postPage = new PageImpl<>(List.of(post));
        when(eventRepository.findByAuthor(eq("author"), any(Pageable.class))).thenReturn(postPage);

        // When
        Page<EventResponseDto> responsePage = eventService.getEventsByAuthor("author", 0, 10, "createdAt");

        // Then
        assertThat(responsePage).isNotNull();
        assertThat(responsePage.getContent()).hasSize(1);
        EventResponseDto eventResponseDto = responsePage.getContent().get(0); // Access first element
        EventResponseDto.EventData eventData = eventResponseDto.getData().get(0); // Access first element
        assertThat(eventData.getTitle()).isEqualTo("Title");
        assertThat(eventResponseDto.getMessage()).isEqualTo("Post retrieved successfully");
    }

    @Test
    void updateEvent() {
        // Given
        Event existingPost = Event.builder()
                .id(1L)
                .title("Old Title")
                .content("Old Content")
                .author(Member.builder().name("author").build())
                .createdAt(LocalDate.now())
                .build();

        EventRequestDto eventRequestDto = EventRequestDto.builder()
                .title("Updated Title")
                .content("Updated Content")
                .author("author")
                .build();

        Event updatedPost = Event.builder()
                .id(1L)
                .title("Updated Title")
                .content("Updated Content")
                .author(Member.builder().name("author").build())
                .createdAt(existingPost.getCreatedAt())
                .updatedAt(LocalDate.now())
                .build();

        when(eventRepository.findById(1L)).thenReturn(Optional.of(existingPost));
        when(memberRepository.findByName("author")).thenReturn(Member.builder().name("author").build());
        when(eventRepository.save(any(Event.class))).thenReturn(updatedPost);

        // When
        EventResponseDto response = eventService.updateEvent(1L, eventRequestDto);

        // Then
        assertThat(response).isNotNull();
        EventResponseDto.EventData eventData = response.getData().get(0); // Access first element
        assertThat(eventData.getTitle()).isEqualTo("Updated Title");
        assertThat(eventData.getContent()).isEqualTo("Updated Content");
        assertThat(response.getMessage()).isEqualTo("Post updated successfully");
    }

    @Test
    void deleteEventById() {
        // Given
        when(eventRepository.existsById(1L)).thenReturn(true);

        // When
        eventService.deleteEventById(1L);

        // Then
        verify(eventRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteByAuthorName() {
        // Given
        EventRequestDto eventRequestDto = EventRequestDto.builder()
                .author("author")
                .build();
        when(memberRepository.findByName("author")).thenReturn(Member.builder().name("author").build());

        // When
        eventService.deleteEventByAuthorName(eventRequestDto);

        // Then
        verify(eventRepository, times(1)).deleteByAuthorName("author");
    }

    @Test
    void getPosts() {
        // Given
        Event post = Event.builder()
                .id(1L)
                .title("Title")
                .content("Content")
                .author(Member.builder().name("author").build())
                .createdAt(LocalDate.now())
                .build();

        Page<Event> postPage = new PageImpl<>(List.of(post));
        when(eventRepository.findAll(any(Pageable.class))).thenReturn(postPage);

        // When
        Page<EventResponseDto> responsePage = eventService.getPosts(PageRequest.of(0, 10));

        // Then
        assertThat(responsePage).isNotNull();
        assertThat(responsePage.getContent()).hasSize(1);
        EventResponseDto eventResponseDto = responsePage.getContent().get(0); // Access first element
        EventResponseDto.EventData eventData = eventResponseDto.getData().get(0); // Access first element
        assertThat(eventData.getTitle()).isEqualTo("Title");
        assertThat(eventResponseDto.getMessage()).isEqualTo("Post retrieved successfully");
        assertThat(eventData.getId()).isEqualTo(1L);
        assertThat(eventData.getContent()).isEqualTo("Content");
        assertThat(eventData.getAuthor()).isEqualTo("author");
        assertThat(eventData.getCreatedAt()).isEqualTo(post.getCreatedAt());
    }
}
