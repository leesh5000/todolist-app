package leesh.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PostWriteRequest {

    private String title;
    private String body;

    @Builder
    public PostWriteRequest(String title, String body) {
        this.title = title;
        this.body = body;
    }

}
