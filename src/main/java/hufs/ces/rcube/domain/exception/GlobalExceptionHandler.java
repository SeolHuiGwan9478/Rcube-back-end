package hufs.ces.rcube.domain.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import hufs.ces.rcube.domain.exception.RestApiException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RestApiException.class)
    public ResponseEntity<ErrorResponse> handleRestApiException(RestApiException e) {
        log.warn("RestApiException 발생: {}", e.getMessage(), e);

        // ErrorCode를 문자열로 변환하고 빈 ValidationErrors 리스트 생성
        String errorCode = e.getErrorCode().name();
        String errorMessage = e.getMessage();
        List<ErrorResponse.ValidationError> validationErrors = new ArrayList<>();

        // ErrorResponse 생성자에 세 개의 매개변수를 전달
        ErrorResponse response = new ErrorResponse(errorCode, errorMessage, validationErrors);

        // ResponseEntity로 에러 응답 반환
        return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> 
    eIllegalArgument(IllegalArgumentException e) {
        log.warn("Invalid Argument Exception: {}", e.getMessage(), e);
        ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
        return handleExceptionInternal(errorCode, e.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Object> handleNullPointerException(NullPointerException e) {
        log.error("Null Pointer Exception: {}", e.getMessage(), e);
        ErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
        return handleExceptionInternal(errorCode, "Unexpected null value encountered.");
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> handleResourceNotFound(ResponseStatusException e) {
        log.warn("Resource Not Found Exception: {}", e.getMessage(), e);
        ErrorCode errorCode = CommonErrorCode.RESOURCE_NOT_FOUND;
        return handleExceptionInternal(errorCode, "Requested resource not found.");
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<Object> handleUnauthorized(SecurityException e) {
        log.warn("Unauthorized Access Exception: {}", e.getMessage(), e);
        ErrorCode errorCode = CommonErrorCode.UNAUTHORIZED;
        return handleExceptionInternal(errorCode, "Access is unauthorized.");
    }

    @ExceptionHandler(RejectedExecutionException.class)
    public ResponseEntity<Object> handleTooManyRequests(RejectedExecutionException e) {
        log.warn("Too Many Requests Exception: {}", e.getMessage(), e);
        ErrorCode errorCode = CommonErrorCode.TOO_MANY_REQUESTS;
        return handleExceptionInternal(errorCode, "Too many requests received. Please try again later.");
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handleServiceUnavailable(IllegalStateException e) {
        log.error("Service Unavailable Exception: {}", e.getMessage(), e);
        ErrorCode errorCode = CommonErrorCode.SERVICE_UNAVAILABLE;
        return handleExceptionInternal(errorCode, "Service is temporarily unavailable.");
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAllException(Exception e) {
        log.error("Unknown Exception: {}", e.getMessage(), e);
        ErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
        return handleExceptionInternal(errorCode, "An unexpected error occurred.");
    }

    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode));
    }

    private ErrorResponse makeErrorResponse(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .build();
    }

    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode, String message) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode, message));
    }

    private ErrorResponse makeErrorResponse(ErrorCode errorCode, String message) {
        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(message)
                .build();
    }
}
