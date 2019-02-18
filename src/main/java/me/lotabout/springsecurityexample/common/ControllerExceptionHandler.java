package me.lotabout.springsecurityexample.common;

import lombok.extern.slf4j.Slf4j;
import me.lotabout.springsecurityexample.common.struct.Response;
import me.lotabout.springsecurityexample.common.struct.ResultException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResultException.class)
    protected ResponseEntity<Response> handleDomainException(
            ResultException ex,
            WebRequest request) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.ok().body(Response.of(ex));
    }
}
