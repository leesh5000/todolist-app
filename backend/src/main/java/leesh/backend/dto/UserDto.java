package leesh.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserDto {

    @JsonProperty("_id")
    private Long id;
    private String username;

    @Builder
    public UserDto(Long id, String username) {
        this.id = id;
        this.username = username;
    }
}
