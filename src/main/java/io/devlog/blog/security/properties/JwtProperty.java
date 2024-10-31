package io.devlog.blog.security.properties;

public interface JwtProperty {
    String SECRET = "cHJvamVjdC1rZXktb2YtYmxvZy1QbGF0Rm9ybS1EZVZMT0cK";
    int ACCESS_EXPIRATION_TIME = 60000 * 60 * 2; // 60분
    int REFRESH_EXPIRATION_TIME = 60000 * 60 * 24 * 7; // 7일
}
