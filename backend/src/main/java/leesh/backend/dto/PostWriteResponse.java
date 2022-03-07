package leesh.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PostWriteResponse {

    private String title;
    private String body;

    @Builder
    public PostWriteResponse(String title, String body) {
        this.title = title;
        this.body = body;
    }

}
