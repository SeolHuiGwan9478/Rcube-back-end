package hufs.ces.rcube.domain.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.FieldError;

import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class ErrorResponse { //예외 발생 시 클라이언트에게 전송할 응답 구조

    private final String code;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY) // erros가 없다면 응답으로 내려가지 않도록
    private final List<ValidationError> errors;

    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class ValidationError { //어느 필드에서 에러가 발생했는지 응답을 위한 내부 정적 클래스

        private final String field;
        private final String message;

        public static ValidationError of(final FieldError fieldError) {
            return ValidationError.builder()
                    .field(fieldError.getField())
                    .message(fieldError.getDefaultMessage())
                    .build();
        }

    }
}