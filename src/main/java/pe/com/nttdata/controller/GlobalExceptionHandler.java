package pe.com.nttdata.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pe.com.nttdata.exception.CustomerException;
import pe.com.nttdata.model.response.ApiErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public static final String ERROR_MESSAGE = "Lo sentimos por favor vuelve a intentar mas tarde.";

    @ExceptionHandler(CustomerException.class)
    public ResponseEntity<ApiErrorResponse> handleCustomerException(CustomerException ex) {

        if (StringUtils.hasText(ex.getMessage())) {
            return ResponseEntity.status(ex.getHttpStatus()).body(new ApiErrorResponse(ex.getCode(),ex.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiErrorResponse("500", ERROR_MESSAGE));
    }

}
