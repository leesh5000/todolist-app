package leesh.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserLoginResponse {

    private Long id;
    private String username;

    @Builder
    public UserLoginResponse(Long id, String username) {
        this.id = id;
        this.username = username;
    }

}
