package leesh.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@Getter
public class PostUpdateRequestDto {

    private String title;
    private String body;
    private Set<String> tags;

    @Builder
    public PostUpdateRequestDto(String title, String body, Set<String> tags) {
        this.title = title;
        this.body = body;
        this.tags = tags;
    }
}
