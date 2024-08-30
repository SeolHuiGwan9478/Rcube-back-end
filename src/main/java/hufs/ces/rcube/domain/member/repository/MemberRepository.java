package hufs.ces.rcube.domain.member.repository;

import hufs.ces.rcube.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {

}
