package leesh.backend.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "post")
@Getter
@Entity
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Lob
    @Column(name = "body")
    private String body;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostTag> postTags = new ArrayList<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private final List<Comment> comments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // 실제 외래키의 주인
    private User user;

    @Builder
    private Post(String title, String body) {
        this.title = title;
        this.body = body;
    }

    //== 생성 매서드 ==//
    public static Post createPost(String title, String body, User user) {
        Post post = Post.builder()
                .title(title)
                .body(body)
                .build();
        post.setUser(user);
        return post;
    }

    //== 연관관계 매서드 ==//
    private void setUser(User user) {
        if (this.user != null) {
            this.user.getPosts().remove(this); // 기존에 존재하는 유저는 삭제
        }
        this.user = user;
        user.getPosts().add(this);
    }

    public void addPostTag(PostTag postTag) {
        this.postTags.add(postTag);
        if (postTag.getPost() != this) {
            postTag.setPost(this);
        }
    }

    //== 비즈니스 로직 ==//
    public void edit(String title, String body, List<PostTag> postTags) {
        if (StringUtils.hasText(title)) {
            this.title = title;
        }
        if (StringUtils.hasText(body)) {
            this.body = body;
        }
        if (postTags != null) { // 빈 값도 가능
            this.postTags.clear();
            this.postTags.addAll(postTags);
        }
    }
}
