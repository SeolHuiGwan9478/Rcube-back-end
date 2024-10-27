package hufs.ces.rcube.domain.post.service;

import hufs.ces.rcube.domain.member.entity.Member;
import hufs.ces.rcube.domain.member.repository.MemberRepository;
import hufs.ces.rcube.domain.post.dto.ProjectRequestDto;
import hufs.ces.rcube.domain.post.dto.ProjectResponseDto;
import hufs.ces.rcube.domain.post.entity.Project;
import hufs.ces.rcube.domain.post.repository.ProjectRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProjectServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveProject() {

        Member member = Member.builder().name("author").build();
        when(memberRepository.findByName("author")).thenReturn(member);

        ProjectRequestDto projectRequestDto = ProjectRequestDto.builder()
                .projectName("Project Name")
                .description("Project Description")
                .author("author")
                .year(2024)
                .imageUrl("https://example.com/image.png")
                .projectLink("https://example.com/project")
                .build();

        Project savedProject = Project.builder()
                .id(1L)
                .projectName("Project Name")
                .description("Project Description")
                .author(member)
                .year(2024)
                .imageUrl("https://example.com/image.png")
                .projectLink("https://example.com/project")
                .createdAt(LocalDate.now())
                .build();

        when(projectRepository.save(any(Project.class))).thenReturn(savedProject);

        // When
        ProjectResponseDto response = projectService.saveProject(projectRequestDto);

        // Then
        assertThat(response).isNotNull();
        ProjectResponseDto.ProjectData projectData = response.getData().get(0);
        assertThat(projectData.getProjectName()).isEqualTo("Project Name");
        assertThat(projectData.getDescription()).isEqualTo("Project Description");
        assertThat(projectData.getAuthor()).isEqualTo("author");
        assertThat(response.getMessage()).isEqualTo("Post created successfully");
    }

    @Test
    void getProjectById() {
        // Given
        Project project = Project.builder()
                .id(1L)
                .projectName("Project Name")
                .description("Project Description")
                .author(Member.builder().name("author").build())
                .year(2024)
                .build();

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        // When
        ProjectResponseDto response = projectService.getProjectById(1L);

        // Then
        assertThat(response).isNotNull();
        ProjectResponseDto.ProjectData projectData = response.getData().get(0);
        assertThat(projectData.getProjectName()).isEqualTo("Project Name");
        assertThat(response.getMessage()).isEqualTo("프로젝트 조회 성공");
    }

    @Test
    void getProjectsByYear() {
        // Given
        Project project = Project.builder()
                .id(1L)
                .projectName("Project Name")
                .description("Project Description")
                .author(Member.builder().name("author").build())
                .year(2024)
                .build();

        when(projectRepository.findByYear(2024)).thenReturn(List.of(project));

        // When
        ProjectResponseDto response = projectService.getProjectByYear(2024);

        // Then
        assertThat(response).isNotNull();
        ProjectResponseDto.ProjectData projectData = response.getData().get(0);
        assertThat(projectData.getProjectName()).isEqualTo("Project Name");
        assertThat(response.getMessage()).isEqualTo("2024년도의 프로젝트 조회 성공");
    }

    @Test
    void updateProject() {
        // Given
        Member member = Member.builder().name("author").build();
        Project existingProject = Project.builder()
                .id(1L)
                .projectName("Old Project Name")
                .description("Old Project Description")
                .author(member)
                .year(2024)
                .build();

        ProjectRequestDto projectRequestDto = ProjectRequestDto.builder()
                .projectName("Updated Project Name")
                .description("Updated Project Description")
                .author("author")
                .year(2024)
                .imageUrl("https://example.com/image.png")
                .projectLink("https://example.com/project")
                .build();

        Project updatedProject = Project.builder()
                .id(1L)
                .projectName("Updated Project Name")
                .description("Updated Project Description")
                .author(existingProject.getAuthor())
                .year(2024)
                .imageUrl("https://example.com/image.png")
                .projectLink("https://example.com/project")
                .build();

        when(projectRepository.findById(1L)).thenReturn(Optional.of(existingProject));
        when(memberRepository.findByName("author")).thenReturn(member);
        when(projectRepository.save(any(Project.class))).thenReturn(updatedProject);

        // When
        ProjectResponseDto response = projectService.updateProject(1L, projectRequestDto);

        // Then
        assertThat(response).isNotNull();
        ProjectResponseDto.ProjectData projectData = response.getData().get(0);
        assertThat(projectData.getProjectName()).isEqualTo("Updated Project Name");
        assertThat(projectData.getDescription()).isEqualTo("Updated Project Description");
        assertThat(response.getMessage()).isEqualTo("Post updated successfully");
    }

    @Test
    void deleteProjectById() {
        // Given
        when(projectRepository.existsById(1L)).thenReturn(true);

        // When
        projectService.deleteProjectById(1L);

        // Then
        verify(projectRepository, times(1)).deleteById(1L);
    }
}
