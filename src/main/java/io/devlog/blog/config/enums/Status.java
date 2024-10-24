package io.devlog.blog.config.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum Status {
    // 200
    OK("confirm", HttpStatus.OK, "성공"),
    // 201
    CREATED("confirm", HttpStatus.CREATED, "생성됨");

    private final String res;
    private final HttpStatus status;
    private final String message;
}
