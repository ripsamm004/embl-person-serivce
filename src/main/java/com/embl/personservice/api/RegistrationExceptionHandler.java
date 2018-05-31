package com.embl.personservice.api;

import com.embl.personservice.api.exception.BadRequestException;
import com.embl.personservice.api.exception.ForbiddenException;
import com.embl.personservice.api.exception.NotFoundException;
import com.embl.personservice.api.exception.ServerException;
import com.embl.personservice.exception.ValidatorGenericException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

@ControllerAdvice
public class RegistrationExceptionHandler {

    Logger logger = Logger.getLogger(RegistrationExceptionHandler.class.getName());

    @ExceptionHandler(ValidatorGenericException.class)
    public ResponseEntity<Object> handleValidationException(ValidatorGenericException ex) {
        logger.log(Level.SEVERE, ex.getMessage());
        return response(ex.getApiError(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(BadRequestException ex) {

        logger.log(Level.SEVERE, ex.getLogMessage());
        return response(ex.getApiError(), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<Object> handleServerException(ServerException ex) {

        logger.log(Level.SEVERE, ex.getLogMessage());
        return response(ex.getApiError(), HttpStatus.INTERNAL_SERVER_ERROR);

    }


    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Object> handleForbiddenException(ForbiddenException ex){
        logger.log(Level.SEVERE, ex.getLogMessage());
        return response(ex.getApiError(), HttpStatus.FORBIDDEN);

    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex){
        logger.log(Level.SEVERE, ex.getLogMessage());
        return response(ex.getApiError(), HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {

        logger.log(Level.SEVERE, ex.getMessage());
        return response(ErrorEnum.INTERNAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);

    }


    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Object> handleGenericThrowable(Throwable ex) {

        logger.log(Level.SEVERE, ex.getMessage());
        return response(ErrorEnum.INTERNAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private static ResponseEntity<Object> response(ErrorEnum errorEnum,
                                                   HttpStatus httpStatus) {
        ApiError apiError = new ApiError(errorEnum.getCode(), errorEnum.getMessage());
        return new ResponseEntity<>(apiError, httpStatus);
    }
}
