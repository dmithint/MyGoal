package org.mygoal.fitnessapp.backend.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.mygoal.fitnessapp.backend.dto.UserDto;
import org.mygoal.fitnessapp.backend.exceptions.AppException;
import org.mygoal.fitnessapp.backend.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 * Provides authentication-related operations, including token generation and validation.
 */
@RequiredArgsConstructor
@Component
public class UserAuthProvider {

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @Value("${security.jwt.token.expiration:86400000}")
    private long expiration;

    private final UserService userService;

    /**
     * Initializes the secret key by encoding it in Base64.
     */
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    /**
     * Generates a JWT token for the given user email.
     *
     * @param email the email of the user
     * @return the generated JWT token
     */
    public String createToken(String email) {
        UserDto user = userService.findByEmail(email);
        Date now = new Date();
        Date validity = new Date(now.getTime() + expiration);
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        return JWT.create()
                .withSubject(email)
                .withClaim("id", user.getId())
                .withClaim("roles", new ArrayList<>(user.getRoles()))
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .sign(algorithm);
    }

    /**
     * Validates the given JWT token and returns the corresponding authentication object.
     *
     * @param token the JWT token to validate
     * @return the authentication object if the token is valid
     * @throws AppException if the token is expired or invalid
     */
    public Authentication validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decoded = verifier.verify(token);

            UserDto user = userService.findByEmail(decoded.getSubject());

            List<GrantedAuthority> authorities = decoded.getClaim("roles").asList(String.class)
                    .stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            return new UsernamePasswordAuthenticationToken(user, null, authorities);
        } catch (JWTVerificationException ex) {
            if (ex instanceof TokenExpiredException) {
                throw new AppException("Token expired", HttpStatus.UNAUTHORIZED);
            }
            throw new AppException("Invalid token", HttpStatus.UNAUTHORIZED);
        }
    }
}
