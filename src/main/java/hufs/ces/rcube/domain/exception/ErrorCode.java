package hufs.ces.rcube.domain.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String name();
    HttpStatus getHttpStatus();
    String getMessage();


    }
