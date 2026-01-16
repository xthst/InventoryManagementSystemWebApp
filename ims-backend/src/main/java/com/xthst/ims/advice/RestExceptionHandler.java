package com.xthst.ims.advice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.xthst.ims.exceptions.ResourceNotFoundException;
import com.xthst.ims.exceptions.ProductLinkedToTransactionException;
import com.xthst.ims.domain.dto.error.*;

@RestControllerAdvice(basePackages = "com.xthst.ims.controllers")
public class RestExceptionHandler {
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiError handleInvalidArgument(MethodArgumentNotValidException ex) {
		
        Map<String, String> validationErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            validationErrors.put(error.getField(), error.getDefaultMessage());
        });
        return new ApiError(
        	HttpStatus.BAD_REQUEST.value(),
        	"Validation Failed",
        	validationErrors
        );
    }
	
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ApiError handleBusinessException(ResourceNotFoundException ex) {
        return new ApiError(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }
    
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ProductLinkedToTransactionException.class)
    public ApiError handleIntegrityViolation(ProductLinkedToTransactionException ex){
    	return new ApiError(HttpStatus.CONFLICT.value(), ex.getMessage());
    }
}


