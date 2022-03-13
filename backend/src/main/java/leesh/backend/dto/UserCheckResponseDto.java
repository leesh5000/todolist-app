package leesh.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserCheckResponseDto {

    private String id;
    private String username;

    @Builder
    public UserCheckResponseDto(String id, String username) {
        this.id = id;
        this.username = username;
    }
}
