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

    public class PostServiceTest {


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
        void savePost() {
            // Given
            Member member = Member.builder().name("author").build();
            when(memberRepository.findByAuthorName("author")).thenReturn(member);

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
            EventResponseDto response = eventService.savePost(eventRequestDto);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.getData().getTitle()).isEqualTo("Title");
            assertThat(response.getData().getContent()).isEqualTo("Content");
            assertThat(response.getData().getAuthor()).isEqualTo("author");
            assertThat(response.getMessage()).isEqualTo("Post created successfully");

        }

        @Test
        void getPostById() {
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
            EventResponseDto response = eventService.getPostById(1L);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.getData().getTitle()).isEqualTo("Title");
            assertThat(response.getMessage()).isEqualTo("Post retrieved successfully");

        }

        @Test
        void getPostByTitle() {
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
            EventResponseDto response = eventService.getPostByTitle("Title");

            // Then
            assertThat(response).isNotNull();
            assertThat(response.getData().getTitle()).isEqualTo("Title");
            assertThat(response.getMessage()).isEqualTo("Post retrieved successfully");

        }

        @Test
        void getPostsByAuthor() {
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
            Page<EventResponseDto> responsePage = eventService.getPostsByAuthor("author", 0, 10, "createdAt");

            // Then
            assertThat(responsePage).isNotNull();
            assertThat(responsePage.getContent()).hasSize(1);
            assertThat(responsePage.getContent().get(0).getData().getTitle()).isEqualTo("Title");
            assertThat(responsePage.getContent().get(0).getMessage()).isEqualTo("Post retrieved successfully");

        }


        @Test
        void updatePost() {
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
            when(memberRepository.findByAuthorName("author")).thenReturn(Member.builder().name("author").build());
            when(eventRepository.save(any(Event.class))).thenReturn(updatedPost);

            // When
            EventResponseDto response = eventService.updatePost(1L, eventRequestDto);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.getData().getTitle()).isEqualTo("Updated Title");
            assertThat(response.getData().getContent()).isEqualTo("Updated Content");
            assertThat(response.getMessage()).isEqualTo("Post updated successfully");

        }

        @Test
        void deletePostById() {
            // Given
            when(eventRepository.existsById(1L)).thenReturn(true);

            // When
            eventService.deletePostById(1L);

            // Then
            verify(eventRepository, times(1)).deleteById(1L);
        }


        @Test
        void deleteByAuthorName() {
            // Given
            EventRequestDto eventRequestDto = EventRequestDto.builder()
                    .author("author")
                    .build();
            when(memberRepository.findByAuthorName("author")).thenReturn(Member.builder().name("author").build());

            // When
            eventService.deleteByAuthorName(eventRequestDto);

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
            assertThat(responsePage.getContent().get(0).getData().getTitle()).isEqualTo("Title");
            assertThat(responsePage.getContent().get(0).getMessage()).isEqualTo("Post retrieved successfully");
            EventResponseDto responseDto = responsePage.getContent().get(0);
            assertThat(responseDto.getData().getId()).isEqualTo(1L);
            assertThat(responseDto.getData().getTitle()).isEqualTo("Title");
            assertThat(responseDto.getData().getContent()).isEqualTo("Content");
            assertThat(responseDto.getData().getAuthor()).isEqualTo("author");
            assertThat(responseDto.getData().getCreatedAt()).isEqualTo(post.getCreatedAt());
            assertThat(responseDto.getMessage()).isEqualTo("Post retrieved successfully");
        }
    }
