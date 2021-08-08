package com.swyrik.kalah.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.swyrik.kalah.exceptions.GameNotAvailableException;
import com.swyrik.kalah.exceptions.InvalidMoveException;
import com.swyrik.kalah.exceptions.InvalidPitIdException;

@ControllerAdvice
public class KalahExceptionHandler extends ResponseEntityExceptionHandler{

	@ExceptionHandler(InvalidPitIdException.class)
	ResponseEntity<String> invalidPitId(InvalidPitIdException ex, WebRequest request){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}
	
	@ExceptionHandler(GameNotAvailableException.class)
	ResponseEntity<String> invalidPitId(GameNotAvailableException ex, WebRequest request){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}
	
	@ExceptionHandler(InvalidMoveException.class)
	ResponseEntity<String> invalidPitId(InvalidMoveException ex, WebRequest request){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}
	
}
