package leesh.backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import leesh.backend.exception.CustomException;
import leesh.backend.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Filter;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import static leesh.backend.common.GlobalProperties.*;
import static leesh.backend.security.JwtUtil.AUTHORITIES_KEY;

@Slf4j
@Filter(name = "JwtFilter")
public class JwtFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;

    public JwtFilter(ObjectMapper objectMapper, JwtUtil jwtUtil) {
        this.objectMapper = objectMapper;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        Optional<String> optional = getAccessTokenFromCookie(request);
        if (optional.isPresent()) {
            try {
                String accessToken = optional.get();
                Claims jwt = jwtUtil.validate(accessToken).getBody();

                if (jwt.get("exp") != null) {
                    long exp = (Integer) jwt.get("exp") * 1000L;
                    long now = System.currentTimeMillis();
                    if (exp - now < ACCESS_TOKEN_REFRESH_MSEC) {

                        log.info("jwt refresh start");
                        log.info("iat = {}", jwt.get("iat"));
                        log.info("exp = {}", exp);
                        CustomUserDetails userDetails = jwtUtil.getUserDetails(accessToken);
                        accessToken = jwtUtil.createAccessToken(userDetails);

                        // set cookie
                        Cookie cookie = new Cookie(X_AUTH, accessToken);
                        cookie.setMaxAge(Math.toIntExact(ACCESS_TOKEN_EXPIRED_MSEC));
                        cookie.setHttpOnly(true);
                        cookie.setPath("/");
                        response.addCookie(cookie);
                    }
                }

                Claims claims = jwtUtil.getClaims(accessToken);
                Collection<? extends GrantedAuthority> authorities =
                        Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList());

                UserAuthentication userAuthentication = new UserAuthentication(claims.getSubject(), claims.get("id"), authorities);
                SecurityContextHolder.getContext().setAuthentication(userAuthentication);
                log.info("SecurityContext saved '{}' uri: {}", userAuthentication.getName(), request.getRequestURI());

            } catch (CustomException e) {
                ErrorResponse body = ErrorResponse.of(e.getErrorCode());
                response.setStatus(e.getErrorCode().getStatus().value());
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(objectMapper.writeValueAsString(body));

                // remove cookie
                Cookie cookie = new Cookie(X_AUTH, null);
                cookie.setMaxAge(0);
                cookie.setHttpOnly(true);
                cookie.setPath("/");
                response.addCookie(cookie);
                return;
            }
        } else {
            log.info("not exist access token");
        }
        log.info("jwt filter end");
        filterChain.doFilter(request, response);
    }

    private Optional<String> getAccessTokenFromCookie(@NotNull HttpServletRequest request) {

        if (request.getCookies() == null) {
            return Optional.empty();
        }

        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(X_AUTH))
                .map(Cookie::getValue)
                .findAny();
    }
}
