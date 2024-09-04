package hufs.ces.rcube.domain.post.repository;

import hufs.ces.rcube.domain.post.entity.Event;
import hufs.ces.rcube.domain.post.entity.Post;
import hufs.ces.rcube.domain.post.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByTitle(String title); //제목으로 게시글 조회
    // 작성자에 대한 게시글 페이징 조회
    Page<Post> findByAuthor(String author, Pageable pageable);

    boolean existsByTitle(String title); //제목으로 조회

    // 이벤트로 게시글 조회
    Page<Post> findByEvent(Event event, Pageable pageable);

    // 프로젝트로 게시글 조회
    Page<Post> findByProject(Project project, Pageable pageable);


    void deleteByAuthorName(String authorName); //작성자로 게시글 삭제
    void deleteByTitle(String title); //제목으로 게시글 삭제

    Page<Post> findAll(Pageable pageable);  // 페이징과 정렬을 지원하는 메서드
   //날짜 범위로 게시글 조회
    Page<Post> findByCreatedAtBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

}