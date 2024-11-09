package hufs.ces.rcube.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "Invalid parameter included"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Resource not exists"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthorized access"),
    TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, "Too many requests"),
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "Service unavailable"),
    NULL_POINTER(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected null value encountered"),
    DUPLICATE_PROJECT_NAME(HttpStatus.BAD_REQUEST, "Duplicate project name"),
    INVALID_AUTHOR(HttpStatus.BAD_REQUEST, "Invalid author name provided"),
    PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "Project not found with the given ID"),
    UNEXPECTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred"),
    FAILED_TO_RETRIEVE_ACCESS_TOKEN(HttpStatus.BAD_REQUEST, "Failed to retrieve access token"),
    FAILED_TO_RETRIEVE_USER_ATTRIBUTES(HttpStatus.BAD_REQUEST, "Failed to retrieve user attributes"),
    INVALID_OAUTH_PROVIDER(HttpStatus.BAD_REQUEST, "Invalid OAuth provider"),
    DUPLICATE_OAUTH_ID(HttpStatus.BAD_REQUEST, "Duplicate OAuth ID");

    private final HttpStatus httpStatus;
    private final String message;



}
