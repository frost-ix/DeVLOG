package io.devlog.blog.security.properties;

public interface JwtProperty {
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
    String SECRET = "blog_by_DeVLOG"; // 비밀값
    int ACCESS_EXPIRATION_TIME = 60000 * 60; // 60분
    int REFRESH_EXPIRATION_TIME = 60000 * 60 * 24 * 7; // 7일
}
