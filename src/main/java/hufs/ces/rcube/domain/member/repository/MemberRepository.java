package hufs.ces.rcube.domain.member.repository;

import hufs.ces.rcube.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberRepository extends JpaRepository<Member,Long> {
    // 작성자 이름으로 게시글 찾기
    Member findByAuthorName(String authorName);
}
