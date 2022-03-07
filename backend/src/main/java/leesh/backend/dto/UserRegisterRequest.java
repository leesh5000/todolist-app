package leesh.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
public class UserRegisterRequest {

    @NotBlank @Length(min = 3, max = 20)
    private String username;

    @NotBlank
    private String password;

    @Builder
    public UserRegisterRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
