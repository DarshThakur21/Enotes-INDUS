package com.enotes.Enotes_INDUS.exceptions;


import com.enotes.Enotes_INDUS.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@Slf4j
@ControllerAdvice
public class GlobalExceptionsHandler {

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> handleNullPointerException(Exception e){
        log.error("GlobalExceptionError :: handleNullPointerException ::",e.getMessage());

//        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return CommonUtil.createErrorResponseMessage(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR );

    }

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<?> handleResourceNotFound(Exception e){
        log.error("controller :: getCategoryDetailsById ::",e.getMessage());

        return CommonUtil.createErrorResponseMessage(e.getMessage(),HttpStatus.NOT_FOUND );
//        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleExecption(Exception e){
        log.error("GlobalExceptionError :: handleExecption::",e.getMessage());

//        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return CommonUtil.createErrorResponseMessage(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR );

    }


    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleValidationException(ValidationException e){
//        log.error("GlobalExceptionError :: handleExecption::",e.getMessage());

//        return new ResponseEntity<>(e.getError(), HttpStatus.BAD_REQUEST);

        return CommonUtil.createErrorResponse(e.getError(),HttpStatus.BAD_REQUEST );

    }

    @ExceptionHandler(ExistDataException.class)
    public ResponseEntity<?> handleExistDataException(ExistDataException e){
//        log.error("GlobalExceptionError :: handleExecption::",e.getMessage());

//        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        return CommonUtil.createErrorResponseMessage(e.getMessage(),HttpStatus.CONFLICT );

    }
//    HttpMessageNotReadableException

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e){
//        log.error("GlobalExceptionError :: handleExecption::",e.getMessage());

//        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        return CommonUtil.createErrorResponseMessage(e.getMessage(),HttpStatus.BAD_REQUEST );

    }



}
