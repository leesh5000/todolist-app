package leesh.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import leesh.backend.entity.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
public class PostGetResponseDto {

    @JsonProperty("_id")
    private String id;
    private String title;
    private String body;
    private Set<String> tags;
    private LocalDateTime publishedDate;
    private LocalDateTime lastModifiedDate;

    @Builder
    private PostGetResponseDto(String id, String title, String body, Set<String> tags, LocalDateTime publishedDate, LocalDateTime lastModifiedDate) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.tags = tags;
        this.publishedDate = publishedDate;
        this.lastModifiedDate = lastModifiedDate;
    }

    public static PostGetResponseDto toDto(Post entity) {
        return PostGetResponseDto.builder()
                .id(String.valueOf(entity.getId()))
                .title(entity.getTitle())
                .body(entity.getBody())
                .tags(entity.getPostTags().stream()
                        .map(postTag -> postTag.getTag().getTagName())
                        .collect(Collectors.toSet()))
                .publishedDate(entity.getCreatedDate())
                .lastModifiedDate(entity.getModifiedDate())
                .build();
    }
}
