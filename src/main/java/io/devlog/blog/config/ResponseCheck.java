package io.devlog.blog.config;

import io.devlog.blog.config.enums.Status;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResponseCheck {
    private final Status es;
    private final String message;
    private final HttpStatus status;
    private final String res;

    public ResponseCheck(Status es) {
        this.es = es;
        this.res = es.getRes();
        this.status = es.getStatus();
        this.message = es.getMessage();
    }
}
