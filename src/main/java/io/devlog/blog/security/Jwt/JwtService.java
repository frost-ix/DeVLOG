package io.devlog.blog.security.Jwt;

import com.auth0.jwt.exceptions.JWTCreationException;
import io.devlog.blog.security.properties.JwtProperty;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Log4j2
@Service
public class JwtService {
    public String createAccessToken(long id) {
        try {
            byte[] secret = Decoders.BASE64.decode(JwtProperty.SECRET);
            Key key = Keys.hmacShaKeyFor(secret);
            return Jwts.builder()
                    .setSubject("user")
                    .claim("id", id)
                    .setExpiration(new Date(System.currentTimeMillis() + JwtProperty.ACCESS_EXPIRATION_TIME))
                    .signWith(key, SignatureAlgorithm.HS256)
                    .compact();

        } catch (JWTCreationException exception) {
            log.error(exception.getStackTrace());
            return null;
        }
    }

    public String createRefreshToken(long id) {
        try {
            byte[] secret = Decoders.BASE64.decode(JwtProperty.SECRET);
            Key key = Keys.hmacShaKeyFor(secret);
            return Jwts.builder()
                    .setSubject("user")
                    .claim("id", id)
                    .setExpiration(new Date(System.currentTimeMillis() + JwtProperty.REFRESH_EXPIRATION_TIME))
                    .signWith(key, SignatureAlgorithm.HS256)
                    .compact();
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

    public boolean validateToken(String token) {
        return token != null && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        log.debug("token: {}", token);
        return getClaims(token).getExpiration().before(new Date());
    }

    public long getAuthorizationId(String token) {
        return getClaims(token).get("id", Long.class);
    }
}