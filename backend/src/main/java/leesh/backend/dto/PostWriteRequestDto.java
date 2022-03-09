package leesh.backend.dto;

import leesh.backend.entity.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@NoArgsConstructor
@Getter
public class PostWriteRequestDto {

    @NotBlank
    private String title;
    private String body;
    private Set<String> tags;

    @Builder
    private PostWriteRequestDto(String title, String body, Set<String> tags) {
        this.title = title;
        this.body = body;
        this.tags = tags;
    }

}
