package hufs.ces.rcube.domain.post.repository;

import hufs.ces.rcube.domain.post.entity.Event;
import hufs.ces.rcube.domain.post.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventRepository extends JpaRepository<Event,Long> {
    Optional<Event> findByTitle(String title); //제목으로 게시글 조회
    // 작성자에 대한 게시글 페이징 조회
    Page<Event> findByAuthor(String author, Pageable pageable);

    boolean existsByTitle(String title); //제목으로 조회

    void deleteByAuthorName(String authorName); //작성자로 게시글 삭제
    void deleteByTitle(String title); //제목으로 게시글 삭제

    Page<Event> findAll(Pageable pageable);  // 페이징과 정렬을 지원하는 메서드

}

