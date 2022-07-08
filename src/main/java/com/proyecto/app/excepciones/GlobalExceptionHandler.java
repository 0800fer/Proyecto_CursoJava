package com.proyecto.app.excepciones;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.ConstraintViolationException;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler
	protected ResponseEntity<ErrorResponse> handleException(NoSuchElementException exc) {
		HttpStatus httpStatus = HttpStatus.NOT_FOUND;
		return buildResponseEntity(httpStatus, exc);
	}

	@ExceptionHandler
	protected ResponseEntity<ErrorResponse> handleException(DuplicateKeyException exc) {
		HttpStatus httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
		return buildResponseEntity(httpStatus, exc);
	}

	@ExceptionHandler
	protected ResponseEntity<ErrorResponse> handleException(IllegalArgumentException exc) {
		HttpStatus httpStatus = HttpStatus.NOT_ACCEPTABLE;
		return buildResponseEntity(httpStatus, exc);
	}

	@ExceptionHandler
	protected ResponseEntity<ErrorResponse> handleException(ConstraintViolationException exc) {
		HttpStatus httpStatus = HttpStatus.NOT_ACCEPTABLE;
		return buildResponseEntity(httpStatus, exc);
	}

	@ExceptionHandler
	protected ResponseEntity<ErrorResponse> handleException(MethodArgumentTypeMismatchException exc) {
		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
		return buildResponseEntity(httpStatus, new RuntimeException("Tipo de Argumento invalido"));
	}

	@ExceptionHandler
	protected ResponseEntity<ErrorResponse> handleException(Exception exc) {
		HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		return buildResponseEntity(httpStatus,
				new RuntimeException("Se presento un problema, reporte e intente luego."));
	}

	private ResponseEntity<ErrorResponse> buildResponseEntity(HttpStatus httpStatus, Exception exc) {
		return buildResponseEntity(httpStatus, exc, null);
	}

	private ResponseEntity<ErrorResponse> buildResponseEntity(HttpStatus httpStatus, Exception exc,
			List<String> errors) {
		ErrorResponse error = new ErrorResponse();
		error.setMessage("USRMSG-" + exc.getMessage());
		error.setStatus(httpStatus.value());
		error.setTimestamp(new Date());
		error.setErrors(errors);
		return new ResponseEntity<>(error, httpStatus);

	}

}
