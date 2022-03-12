package leesh.backend.dto;

import leesh.backend.entity.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
public class PostGetResponseDto {

    private Long id;
    private String title;
    private String body;
    private Set<String> tag;
    private String author;
    private LocalDateTime publishedDate;
    private LocalDateTime lastModifiedDate;

    @Builder
    private PostGetResponseDto(Long id, String title, String body, Set<String> tag, String author, LocalDateTime publishedDate, LocalDateTime lastModifiedDate) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.tag = tag;
        this.author = author;
        this.publishedDate = publishedDate;
        this.lastModifiedDate = lastModifiedDate;
    }

    public static PostGetResponseDto toDto(Post entity) {
        return PostGetResponseDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .body(entity.getBody())
                .author(entity.getUser().getUsername())
                .tag(entity.getPostTags().stream()
                        .map(postTag -> postTag.getTag().getTagName())
                        .collect(Collectors.toSet()))
                .publishedDate(entity.getCreatedDate())
                .lastModifiedDate(entity.getModifiedDate())
                .build();
    }
}
