package leesh.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

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
    public PostWriteResponseDto(Long id, String title, String body, Set<String> tag, String author, LocalDateTime publishedDate) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.tag = tag;
        this.author = author;
        this.publishedDate = publishedDate;
    }
}
