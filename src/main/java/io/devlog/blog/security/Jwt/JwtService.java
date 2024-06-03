package io.devlog.blog.security.Jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import io.devlog.blog.security.properties.JwtProperty;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Date;

@Log4j2
@Service
public class JwtService {
    public String createAccessToken(String id) {
        try {
            return JwtProperty.TOKEN_PREFIX + JWT.create()
                    .withSubject(id)
                    .withClaim("id", id)
                    .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperty.ACCESS_EXPIRATION_TIME))
                    .sign(Algorithm.HMAC256(JwtProperty.SECRET));
        } catch (JWTCreationException exception) {
            log.error(exception.getStackTrace());
            return null;
        }
    }

    public String createRefreshToken(String id, String accessToken) {
        try {
            return JWT.create()
                    .withSubject(id)
                    .withClaim("id", id)
                    .withClaim("accessToken", accessToken)
                    .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperty.REFRESH_EXPIRATION_TIME))
                    .sign(Algorithm.HMAC256(JwtProperty.SECRET));
        } catch (JWTCreationException exception) {
            log.error(exception.getStackTrace());
            return null;
        }
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(JwtProperty.SECRET)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token, String username) {
        final String extractedUsername = getClaims(token).getSubject();
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }
}