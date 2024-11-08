package hufs.ces.rcube.domain.execption;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {
    // 기존 에러 코드들
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "Invalid parameter included"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Resource not exists"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthorized access"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "Method not allowed"),
    TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, "Too many requests"),
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "Service unavailable"),
    NULL_POINTER(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected null value encountered"),

    // 새로운 에러 코드들 추가
    DUPLICATE_PROJECT_NAME(HttpStatus.BAD_REQUEST, "Duplicate project name"),
    INVALID_AUTHOR(HttpStatus.BAD_REQUEST, "Invalid author name provided"),
    PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "Project not found with the given ID"),
    UNEXPECTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred");

    private final HttpStatus httpStatus;
    private final String message;
}
