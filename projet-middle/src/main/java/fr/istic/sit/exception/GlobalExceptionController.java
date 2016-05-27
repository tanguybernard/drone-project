package fr.istic.sit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author FireDroneTeam
 */

@ControllerAdvice
public class GlobalExceptionController {

    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    @ExceptionHandler(value = CustomException.class)
    public ErrorResponse handleBaseException(CustomException e){
        return new ErrorResponse(e.getCode(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public ErrorResponse handleException(Exception e){
        return new ErrorResponse(e.getMessage());
    }

}
