package leesh.backend.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    @ElementCollection(fetch = FetchType.LAZY)
    private Set<String> tag;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private final List<Comment> comments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // 실제 외래키의 주인
    private User user;

    private Post(String title, String body, Set<String> tag, User user) {
        this.title = title;
        this.body = body;
        this.tag = tag;
        this.user = user;
    }

    public static Post write(String title, String body, Set<String> tag, User author) {
        return new Post(title, body, tag, author);
    }

    public void edit(String title, String body, Set<String> tag) {
        if (StringUtils.hasText(title)) {
            this.title = title;
        }
        if (StringUtils.hasText(body)) {
            this.body = body;
        }
        if (tag != null) { // 빈 값도 가능
            this.tag = tag;
        }
    }
}
