package leesh.backend.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class PostTag extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "post_tag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    //== 생성 매서드 ==//
    public static PostTag createPostTag(Post post, Tag tag) {
        PostTag postTag = new PostTag();
        postTag.setPost(post);
        postTag.setTag(tag);
        return postTag;
    }

    //== 연관관계 매서드 ==//
    public void setPost(Post post) {
        if (this.post != null) {
            this.post.getPostTags().remove(this);
        }
        this.post = post;
        if (!post.getPostTags().contains(this)) {
            post.addPostTag(this);
        }
    }

    public void setTag(Tag tag) {
        if (this.tag != null) {
            this.tag.getPostTags().remove(this);
        }
        this.tag = tag;
        if (!tag.getPostTags().contains(this)) {
            tag.addPostTag(this);
        }
    }

}
