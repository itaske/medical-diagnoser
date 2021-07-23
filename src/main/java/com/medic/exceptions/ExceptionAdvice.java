package com.medic.exceptions;

import com.medic.responses.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityExistsException;
import java.net.UnknownHostException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<ErrorResponse> badRequestHandler(BadRequestException e){
        return buildErrorResponse(e, HttpStatus.BAD_REQUEST);
    }

   @ExceptionHandler(value = EntityExistsException.class)
   public ResponseEntity<ErrorResponse> entityExist(EntityExistsException e){
        return buildErrorResponse(e, HttpStatus.BAD_REQUEST);
   }


    @ExceptionHandler(value = ForbiddenRequestException.class)
    public ResponseEntity<ErrorResponse> forbiddenRequestHandler(ForbiddenRequestException e){
        return buildErrorResponse(e, HttpStatus.FORBIDDEN);
    }


    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Map<String, String> handleValidations(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }


    @ExceptionHandler(value =ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> resourceNotFoundHandler(ResourceNotFoundException e){
        return buildErrorResponse(e, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> sqlConstraintViolation(SQLIntegrityConstraintViolationException ex){
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = InterruptedRequestException.class)
    public ResponseEntity<ErrorResponse> requestWasInterrupted(InterruptedRequestException e){
        return buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = UnknownHostException.class)
    public ResponseEntity<ErrorResponse> unknownHost(UnknownHostException e){
        return requestWasInterrupted(new InterruptedRequestException(e.getMessage()));
    }



    private ResponseEntity<ErrorResponse> buildErrorResponse(Exception e, HttpStatus httpStatus){
        ErrorResponse response = new ErrorResponse();
        response.setCode(httpStatus.value());
        response.setStatus(httpStatus.toString());
        response.setMessage(e.getMessage());
        response.setTimestamp(LocalDateTime.now().toString());
        return new ResponseEntity<>(response, httpStatus);
    }


}
