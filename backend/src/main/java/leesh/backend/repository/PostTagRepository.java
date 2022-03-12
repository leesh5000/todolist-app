package leesh.backend.repository;

import leesh.backend.entity.Post;
import leesh.backend.entity.PostTag;
import leesh.backend.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {

    boolean existsByPostAndTag(Post post, Tag tag);
}
