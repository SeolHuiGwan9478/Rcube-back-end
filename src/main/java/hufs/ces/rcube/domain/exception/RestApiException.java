package hufs.ces.rcube.domain.exception;

import lombok.Getter;

@Getter
public class RestApiException extends RuntimeException {
    private final ErrorCode errorCode;  // ErrorCode는 CommonErrorCode와 같은 에러 코드 인터페이스를 의미

    // 생성자
    public RestApiException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
