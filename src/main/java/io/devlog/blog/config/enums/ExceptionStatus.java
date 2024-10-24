package io.devlog.blog.config.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ExceptionStatus {
    // 204
    NO_CONTENT("no_content", HttpStatus.NO_CONTENT, "콘텐츠 없음"),
    // 304
    NOT_MODIFIED("not_modified", HttpStatus.NOT_MODIFIED, "수정되지 않음"),
    // 400
    BAD_REQUEST("error", HttpStatus.BAD_REQUEST, "잘못된 요청"),
    // 404
    NOT_FOUND("error_not_found_page", HttpStatus.NOT_FOUND, "페이지 찾을 수 없음"),
    // User Not Found
    USER_NOT_FOUND("error_not_found", HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    // 401
    UNAUTHORIZED("error_auth", HttpStatus.UNAUTHORIZED, "인증 실패"),
    // 403
    FORBIDDEN("error_forbidden", HttpStatus.FORBIDDEN, "권한 없음"),
    // 405
    METHOD_NOT_ALLOWED("error_not_allowed", HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 메소드"),
    // 409
    CONFLICT("error_conflict", HttpStatus.CONFLICT, "이미 존재하는 데이터"),
    // 500
    SERVER_ERROR("error_server", HttpStatus.INTERNAL_SERVER_ERROR, "서버에러!"),
    // 503
    SERVICE_UNAVAILABLE("error_SERVICE_UNAVAILABLE", HttpStatus.SERVICE_UNAVAILABLE, "서비스 이용 불가"),
    // 504
    GATEWAY_TIMEOUT("error_TIMEOUT", HttpStatus.GATEWAY_TIMEOUT, "서버 접속 실패");

    private final String res;
    private final HttpStatus status;
    private final String message;
}
