package hufs.ces.rcube.domain.post.repository;

import hufs.ces.rcube.domain.post.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByYear(int year);
}
