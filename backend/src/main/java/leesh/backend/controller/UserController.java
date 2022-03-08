package leesh.backend.controller;

import leesh.backend.dto.UserLoginRequest;
import leesh.backend.dto.UserLoginResponse;
import leesh.backend.dto.UserRegisterRequest;
import leesh.backend.dto.UserRegisterResponse;
import leesh.backend.entity.User;
import leesh.backend.exception.CustomException;
import leesh.backend.security.CustomUserDetailsService;
import leesh.backend.security.JwtUtil;
import leesh.backend.security.UserAuthentication;
import leesh.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import static leesh.backend.common.GlobalProperties.ACCESS_TOKEN_EXPIRED_MSEC;
import static leesh.backend.common.GlobalProperties.X_AUTH;
import static leesh.backend.exception.ErrorCode.INVALID_PASSWORD;
import static leesh.backend.exception.ErrorCode.NOT_EXIST_USER;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;

    @PostMapping("/api/users/register")
    public ResponseEntity<UserRegisterResponse> register(@RequestBody @Validated UserRegisterRequest requestDto) {

        User savedUser = userService.register(requestDto);

        UserRegisterResponse body = UserRegisterResponse.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PostMapping("/api/users/login")
    public ResponseEntity<UserLoginResponse> login(@RequestBody @Validated UserLoginRequest requestDto, HttpServletResponse response) {

        User loginUser = userService.login(requestDto);
        UserAuthentication userAuthentication = new UserAuthentication(requestDto.getUsername(), requestDto.getPassword());

        UserDetails userDetails = null;
        try {
            userDetails = (UserDetails) authenticationManager.authenticate(userAuthentication).getPrincipal();
        } catch (DisabledException | LockedException e){
            // TODO 추후 추가할 것
        } catch (UsernameNotFoundException e) {
            throw new CustomException(NOT_EXIST_USER);
        } catch (BadCredentialsException e) {
            throw new CustomException(INVALID_PASSWORD);
        }

        UserLoginResponse body = UserLoginResponse.builder()
                .id(loginUser.getId())
                .username(loginUser.getUsername())
                .build();

        assert userDetails != null;
        String accessToken = jwtUtil.createAccessToken(userDetails);

        // set cookie
        Cookie cookie = new Cookie(X_AUTH, accessToken);
        cookie.setMaxAge(Math.toIntExact(ACCESS_TOKEN_EXPIRED_MSEC));
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @GetMapping("/api/users/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {

        // set cookie
        Cookie cookie = new Cookie(X_AUTH, null);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }



}
