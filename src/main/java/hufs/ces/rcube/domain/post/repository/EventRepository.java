package hufs.ces.rcube.domain.post.repository;

import hufs.ces.rcube.domain.post.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event,Long> {
}

