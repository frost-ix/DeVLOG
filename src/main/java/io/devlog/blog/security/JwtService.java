package io.devlog.blog.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import io.devlog.blog.security.properties.JwtProperty;
import io.devlog.blog.user.DTO.UserDTO;
import lombok.extern.log4j.Log4j2;

import java.util.Date;

@Log4j2
public class JwtService {
    public String createAccessToken(UserDTO user) {
        try {
            return JWT.create()
                    .withSubject(user.getId())
                    .withClaim("role", user.getAccessRole().name())
                    .withClaim("name", user.getName())
                    .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperty.ACCESS_EXPIRATION_TIME))
                    .sign(Algorithm.HMAC256(JwtProperty.SECRET));
        } catch (JWTCreationException exception) {
            log.error(exception.getStackTrace());
            return null;
        }
    }

    public String createRefreshToken(UserDTO user, String accessToken) {
        try {
            return JWT.create()
                    .withSubject(user.getId())
                    .withClaim("accessToken", accessToken)
                    .withClaim("role", user.getAccessRole().name())
                    .withClaim("name", user.getName())
                    .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperty.REFRESH_EXPIRATION_TIME))
                    .sign(Algorithm.HMAC256(JwtProperty.SECRET));
        } catch (JWTCreationException exception) {
            log.error(exception.getStackTrace());
            return null;
        }
    }
}
