package hufs.ces.rcube.domain.execption;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RestApiException extends RuntimeException { //언체크예외를 상속받는 예외 클래스

    private final ErrorCode errorCode;

}

