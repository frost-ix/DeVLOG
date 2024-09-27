package io.devlog.blog.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorStatus {
    // 400
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청"),
    // 404
    NOT_FOUND(HttpStatus.NOT_FOUND, "페이지 찾을 수 없음"),
    // User Not Found
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    // 500
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에러!");

    private final HttpStatus status;
    private final String message;
}
