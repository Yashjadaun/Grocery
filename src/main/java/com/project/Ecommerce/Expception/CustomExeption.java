package com.project.Ecommerce.Expception;

import com.project.Ecommerce.Expception.AllExeption.ApiExpetion;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExeption {

    @ExceptionHandler(ApiExpetion.class)
     public ResponseEntity<?> myApiExpetion(ApiExpetion e){
          String message =e.getMessage();
           return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

}
