package leesh.backend.repository;

import leesh.backend.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    Tag findByTagName(String tagName);

    boolean existsTagByTagName(String tagName);
}
