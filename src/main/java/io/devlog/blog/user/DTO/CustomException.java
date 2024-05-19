package io.devlog.blog.user.DTO;

import io.devlog.blog.user.enums.ErrorStatus;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ErrorStatus es;
    private final String message;
    private final String res;

    public CustomException(ErrorStatus es) {
        this.res = "error exception";
        this.es = es;
        this.message = es.getMessage();
    }
}
