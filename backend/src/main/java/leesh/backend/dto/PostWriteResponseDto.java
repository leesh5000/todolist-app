package leesh.backend.dto;

import leesh.backend.entity.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
public class PostWriteResponseDto {

    private Long id;
    private String title;
    private String body;
    private Set<String> tag;
    private String author;
    private LocalDateTime publishedDate;

    @Builder
    private PostWriteResponseDto(Long id, String title, String body, Set<String> tag, String author, LocalDateTime publishedDate) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.tag = tag;
        this.author = author;
        this.publishedDate = publishedDate;
    }

    public static PostWriteResponseDto toDto(Post entity) {
        return PostWriteResponseDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .body(entity.getBody())
                .author(entity.getUser().getUsername())
                .tag(entity.getPostTags().stream()
                        .map(postTag -> postTag.getTag().getTagName())
                        .collect(Collectors.toSet()))
                .publishedDate(entity.getCreatedDate())
                .build();
    }
}
