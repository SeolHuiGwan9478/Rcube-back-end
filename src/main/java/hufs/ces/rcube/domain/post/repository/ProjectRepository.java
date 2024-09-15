package hufs.ces.rcube.domain.post.repository;

import hufs.ces.rcube.domain.post.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
