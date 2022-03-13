package leesh.backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import leesh.backend.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

import static leesh.backend.common.GlobalProperties.ACCESS_TOKEN_EXPIRED_MSEC;
import static leesh.backend.exception.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtUtil implements InitializingBean {

    public static final String AUTHORITIES_KEY = "auth";
    public static final String ISSUER = "TODO-APP";
    private final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    public String createAccessToken(@NotNull CustomUserDetails userDetails) {

        Instant issuedAt = Instant.now();
        Instant expiredAt = issuedAt.plus(ACCESS_TOKEN_EXPIRED_MSEC, ChronoUnit.MILLIS);

        String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setIssuer(ISSUER)
                .setIssuedAt(Date.from(issuedAt))
                .setExpiration(Date.from(expiredAt))
                .setSubject(userDetails.getUsername())
                .claim("id", userDetails.getId())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public CustomUserDetails getUserDetails(@NotNull String accessToken) {

        Claims claims = getClaims(accessToken);

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        return customUserDetailsService.loadUserByUsername(claims.getSubject());
    }

    public Claims getClaims(@NotNull String jwt) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    public Jws<Claims> validate(@NotNull String jwt) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt);
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            throw new CustomException(INVALID_JWT);
        } catch (ExpiredJwtException e) {
            throw new CustomException(EXPIRED_ACCESS_TOKEN);
        } catch (UnsupportedJwtException | IllegalArgumentException e) {
            throw new CustomException(INVALID_INPUT_VALUE);
        }
    }

}
