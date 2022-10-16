package com.acme.test.errors;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;



@ControllerAdvice
public class ErrorHandler {
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity validationErrorHandler(ConstraintViolationException ex) {
		
		List<String> errorList = new ArrayList<String>();
		Set<ConstraintViolation<?>> errors = ex.getConstraintViolations();
		for (ConstraintViolation<?> constraintViolation : errors) {
			errorList.add(  constraintViolation.getMessage()  );
}
	
		return new ResponseEntity(errorList, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(AlreadyInsertedException.class)
	public ResponseEntity<String> alreadyExistHandler(AlreadyInsertedException aiex) {
		
		return new ResponseEntity<String>( aiex.getMessage( ), HttpStatus.BAD_REQUEST  );
		
	}
	
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<String> entityNotFoundHandler(EntityNotFoundException enfex) {
		return new ResponseEntity<String> (enfex.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(AlreadyDeletedException.class)
	public ResponseEntity<String> alreadyDeletedHandler(AlreadyDeletedException adex) {
		return new ResponseEntity<String> (adex.getMessage(), HttpStatus.NOT_FOUND);
	}

}
