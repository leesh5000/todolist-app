package leesh.backend.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static leesh.backend.exception.ErrorCode.INVALID_INPUT_VALUE;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        log.error("handleMethodArgumentNotValidException", e);

        ErrorResponse payload = ErrorResponse.of(INVALID_INPUT_VALUE, e.getBindingResult());

        return ResponseEntity.status(payload.getStatus()).body(payload);
    }

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<?> handleCustomException(CustomException e) {
        log.error("handleCustomException", e);

        ErrorResponse payload = ErrorResponse.of(e.getErrorCode());

        return ResponseEntity.status(payload.getStatus()).body(payload);
    }

}
