package io.devlog.blog.config;

import io.devlog.blog.config.enums.ExceptionStatus;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {
    private final ExceptionStatus es;
    private final String message;
    private final HttpStatus status;
    private final String res;

    public CustomException(ExceptionStatus s) {
        this.es = s;
        this.res = s.getRes();
        this.status = s.getStatus();
        this.message = s.getMessage();
    }
}
