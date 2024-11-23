package hufs.ces.rcube.domain.member.repository;

import hufs.ces.rcube.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member,Long> {
    // 작성자 이름으로 게시글 찾기
    Member findByName(String authorName);
    Optional<Member> findByOauthId(String oauthId);
    long count(); //총 학회원 수를 가져오는 메서드
}
