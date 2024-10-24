package io.devlog.blog.config;

import io.devlog.blog.config.enums.Status;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResponseCheck {
    private final Status s;
    private final String message;
    private final HttpStatus status;
    private final String res;

    public ResponseCheck(Status s) {
        this.s = s;
        this.res = s.getRes();
        this.status = s.getStatus();
        this.message = s.getMessage();
    }
}
