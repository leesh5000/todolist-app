package leesh.backend.service;

import leesh.backend.dto.UserLoginRequest;
import leesh.backend.dto.UserRegisterRequest;
import leesh.backend.entity.User;
import leesh.backend.exception.CustomException;
import leesh.backend.exception.ErrorCode;
import leesh.backend.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Getter
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(UserRegisterRequest requestDto) {

        userRepository.findByUsername(requestDto.getUsername())
                .ifPresent(e -> {
                    throw new CustomException(ErrorCode.ALREADY_EXIST_USER);
                });

        User user = User.builder()
                .username(requestDto.getUsername())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .build();

        return userRepository.save(user);
    }
}
