package com.hattori.payment.exception;


import com.hattori.payment.model.response.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException userNotFoundException, WebRequest webRequest){
        return new ResponseEntity<>(new ErrorResponse("Пользователь не найден","Повторите запрос либо обратитесь с службу поддержки"), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        int size = ex.getBindingResult().getFieldErrors().size();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++){
            sb.append(ex.getBindingResult().getFieldErrors().get(i).getDefaultMessage()+"\n");
        }
        return new ResponseEntity<>(new ErrorResponse("Проверьте корректность данных",sb.toString()),HttpStatus.BAD_REQUEST);
    }
}
