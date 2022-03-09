package leesh.backend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    /**
     *
     */
    INVALID_INPUT_VALUE(BAD_REQUEST, 4000001, "invalid input value"),
    INVALID_PASSWORD(BAD_REQUEST, 4000002, "invalid password"),

    NOT_FOUND_ACCESS_TOKEN(UNAUTHORIZED, 4010001, "access token not found in header field"),
    INVALID_JWT(UNAUTHORIZED, 4010002, "The token was signed incorrectly"),
    EXPIRED_ACCESS_TOKEN(UNAUTHORIZED, 4010003, "The access token was expired"),

    NOT_FOUND_RESOURCE(NOT_FOUND, 4040001, "not found resource"),
    ALREADY_EXIST_USER(CONFLICT, 4090001, "already exist user"),

    NO_PERMISSION(HttpStatus.FORBIDDEN, 4030001, "no permission"),

    /**
     *
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 5000001, "internal server error")

    ;

    private final HttpStatus status;
    private final Integer code;
    private final String message;

}
