package hufs.ces.rcube.domain.post.repository;

import hufs.ces.rcube.domain.post.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    // 연도별 프로젝트 조회
    List<Project> findByYear(int year);

    // 프로젝트 이름으로 중복 여부 확인 (이미 존재하는 프로젝트 이름이 있는지 확인)
    boolean existsByProjectName(String projectName);

}
