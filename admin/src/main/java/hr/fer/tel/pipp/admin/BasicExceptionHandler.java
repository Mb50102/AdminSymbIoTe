package hr.fer.tel.pipp.admin;

import eu.h2020.symbiote.security.commons.exceptions.custom.AAMException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class BasicExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException e) {
        return handle(e, "400", "Bad request", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> handleNullPointer(NullPointerException e) {
        return handle(e, "400", "Bad request", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AAMException.class)
    public ResponseEntity<?> handleAAMException(AAMException e) {
        return handle(e, "500", "registracija na symbiote nije uspjela", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    private ResponseEntity<?> handleEntityNotFound(EntityNotFoundException e) {
        return handle(e, "404", "nema", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadCredentialsException.class)
    private ResponseEntity<?> handleBadCredentials(BadCredentialsException e) {
        return handle(e, "401", "Bad credentials", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    private ResponseEntity<?> handleAccessDenied(AccessDeniedException e) {
        return handle(e, "403", "Forbidden", HttpStatus.FORBIDDEN);
    }

    private ResponseEntity<?> handle(Exception e, String status, String error, HttpStatus httpStatus) {
        Map<String, String> props = new HashMap<>();
        props.put("message", e.getMessage());
        props.put("status", status);
        props.put("error", e.getClass().getSimpleName());

        return new ResponseEntity<>(props, httpStatus);
    }

}
