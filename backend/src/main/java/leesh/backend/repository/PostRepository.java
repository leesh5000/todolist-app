package leesh.backend.repository;

import leesh.backend.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

//    @Query("select p from Post p join fetch p.user, p.comments")
//    public Page<Post> findAllByUsernameByOrderByIdDesc(String username, Pageable pageable);
//
//    // N+1 문제 주의 -> fetch join 으로 해결
//    @Query("select p from Post p join fetch p.user u where u.username = :username")
//    public List<Post> findByUsername(@Param("username") String username, Pageable pageable);

    // N+1 문제 주의 -> fetch join 으로 해결
    @Query("select p from Post p where p.user.username = :username")
    public List<Post> findByUsername(@Param("username") String username);

    public List<Post> findTop10ByOrderByIdDesc();

}
