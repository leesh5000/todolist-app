package leesh.backend.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tag")
@Entity
public class Tag extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "tag_id")
    private Long id;

    @Column(name = "tag_name", unique = true)
    private String tagName;

    @OneToMany(mappedBy = "tag", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PostTag> postTags = new ArrayList<>();

    @Builder
    private Tag(String tagName) {
        this.tagName = tagName;
    }

    //== 생성 매서드 ==//
    public static Tag createTag(String tagName) {
        return Tag.builder()
                .tagName(tagName)
                .build();
    }

    //== 연관관계 매서드 ==//
    public void addPostTag(PostTag postTag) {
        this.postTags.add(postTag);
        if (postTag.getTag() != this) {
            postTag.setTag(this);
        }
    }
}
