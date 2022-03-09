package leesh.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@Getter
public class PostUpdateResponseDto {

    private Long id;
    private String title;
    private String body;
    private Set<String> tag;
    private String author;
    private LocalDateTime publishedDate;
    private LocalDateTime lastModifiedDate;

    @Builder
    public PostUpdateResponseDto(Long id, String title, String body, Set<String> tag, String author, LocalDateTime publishedDate, LocalDateTime lastModifiedDate) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.tag = tag;
        this.author = author;
        this.publishedDate = publishedDate;
        this.lastModifiedDate = lastModifiedDate;
    }

}
