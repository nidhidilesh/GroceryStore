package com.grocery.GroceryStore.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	public ResponseEntity<Object> handleNotFoundException(Exception nfe) {
		log.error(nfe.getMessage());
		nfe.printStackTrace();
		return new ResponseEntity<Object>(nfe.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	public ResponseEntity<String> handleOutOfStockException(Exception oose) {
		log.error(oose.getMessage());
		oose.printStackTrace();
		return new ResponseEntity<String>(oose.getMessage(), HttpStatus.NOT_FOUND);
	}

}
