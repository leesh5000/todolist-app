package leesh.backend.controller;

import leesh.backend.dto.UserLoginRequest;
import leesh.backend.dto.UserLoginResponse;
import leesh.backend.dto.UserRegisterRequest;
import leesh.backend.dto.UserRegisterResponse;
import leesh.backend.entity.User;
import leesh.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/api/users/register")
    public ResponseEntity<UserRegisterResponse> register(@RequestBody @Validated UserRegisterRequest requestDto) {

        User savedUser = userService.register(requestDto);

        UserRegisterResponse body = UserRegisterResponse.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @GetMapping("/api/users/login")
    public ResponseEntity<UserLoginResponse> login(@RequestBody @Validated UserLoginRequest requestDto) {

        User loginUser = userService.login(requestDto);

        UserLoginResponse body = UserLoginResponse.builder()
                .id(loginUser.getId())
                .username(loginUser.getUsername())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

}
