package io.devlog.blog.security.properties;

public interface JwtProperty {
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
    // == key-of-blog-PlatForm-DeVLOG-Project
    String SECRET = "a2V5LW9mLWJsb2ctUGxhdEZvcm0tRGVWTE9HLVByb2plY3QK"; // 비밀값
    int ACCESS_EXPIRATION_TIME = 60000 * 60; // 60분
    int REFRESH_EXPIRATION_TIME = 60000 * 60 * 24 * 7; // 7일
}
