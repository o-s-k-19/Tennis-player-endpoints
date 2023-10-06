package com.tennis.player.controller.advice;

import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.tennis.player.exceptions.CountryAlreadyExistException;
import com.tennis.player.exceptions.CountryNotFoundException;
import com.tennis.player.exceptions.PlayerNotFoundException;
import com.tennis.player.model.ErrorResponse;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Cette méthode gére l'exception de type MethodArgumentTypeMismatchException
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
	    HttpHeaders headers, HttpStatusCode status, WebRequest request) {

	Map<String, Object> validationErrors = exception.getBindingResult().getFieldErrors().stream()
		.collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
	HttpStatus httpStatus = HttpStatus.valueOf(status.value());
	ErrorResponse<Map<String, Object>> body = new ErrorResponse<>(exception, httpStatus, request, validationErrors);

	return new ResponseEntity<>(body, httpStatus);
    }

    /**
     * Cette méthode gére l'exception de type MethodArgumentTypeMismatchException
     */
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
	    HttpRequestMethodNotSupportedException exception, HttpHeaders headers, HttpStatusCode status,
	    WebRequest request) {

	String localizedMessage = exception.getLocalizedMessage();
	String message = (StringUtils.isNotEmpty(localizedMessage) ? localizedMessage
		: HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase());
	HttpStatus httpStatus = HttpStatus.METHOD_NOT_ALLOWED;
	ErrorResponse<Map<String, String>> body = new ErrorResponse<>(exception, httpStatus, request,
		Map.of("error", message));

	return new ResponseEntity<>(body, httpStatus);
    }

    /**
     * Cette méthode gére l'exception de type Missing path variable
     */
    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException exception,
	    HttpHeaders headers, HttpStatusCode status, WebRequest request) {

	String localizedMessage = exception.getLocalizedMessage();
	String message = (StringUtils.isNotEmpty(localizedMessage) ? localizedMessage
		: HttpStatus.BAD_REQUEST.getReasonPhrase());
	HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
	ErrorResponse<Map<String, String>> body = new ErrorResponse<>(exception, httpStatus, request,
		Map.of("error", message));
	return new ResponseEntity<>(body, httpStatus);
    }

    /**
     * Cette méthode gére l'exception de constraint validation
     */
    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException exception,
	    WebRequest request) {
	Map<Object, Object> validationErrors = exception.getConstraintViolations().stream()
		.collect(Collectors.toMap(ConstraintViolation::getPropertyPath, ConstraintViolation::getMessage));

	HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
	ErrorResponse<Map<Object, Object>> body = new ErrorResponse<>(exception, httpStatus, request, validationErrors);

	return new ResponseEntity<>(body, httpStatus);
    }

    /**
     * Cette méthode gére toutes les exceptions d'une façon génerale
     */
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAllExceptions(Exception exception, WebRequest request) {

	ResponseStatus responseStatus = exception.getClass().getAnnotation(ResponseStatus.class);
	HttpStatus httpStatus = responseStatus != null ? responseStatus.value() : HttpStatus.INTERNAL_SERVER_ERROR;
	String localizedMessage = exception.getLocalizedMessage();
	String message = (StringUtils.isNotEmpty(localizedMessage) ? localizedMessage : httpStatus.getReasonPhrase());
	ErrorResponse<Map<String, String>> body = new ErrorResponse<>(exception, httpStatus, request,
		Map.of("error", message));

	return new ResponseEntity<>(body, httpStatus);
    }

    /**
     * Cette méthode gére l'exception de type NotFoundException
     */
    @ExceptionHandler({ PlayerNotFoundException.class, CountryNotFoundException.class })
    public ResponseEntity<Object> handleNotFoundException(Exception exception, WebRequest request) {

	String localizedMessage = exception.getLocalizedMessage();
	HttpStatus httpStatus = HttpStatus.NOT_FOUND;
	String message = (StringUtils.isNotEmpty(localizedMessage) ? localizedMessage : httpStatus.getReasonPhrase());
	ErrorResponse<Map<String, String>> body = new ErrorResponse<>(exception, httpStatus, request,
		Map.of("error", message));

	return new ResponseEntity<>(body, httpStatus);
    }

    /**
     * Cette méthode gére l'exception de type IllegalStateException
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handleIllegalStateException(Exception exception, WebRequest request) {
	HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
	String localizedMessage = exception.getLocalizedMessage();
	String message = (StringUtils.isNotEmpty(localizedMessage) ? localizedMessage : httpStatus.getReasonPhrase());

	ErrorResponse<Map<String, String>> body = new ErrorResponse<>(exception, httpStatus, request,
		Map.of("error", message));

	return new ResponseEntity<>(body, httpStatus);
    }

    /**
     * Cette méthode gére l'exception de type BadCredentialsException, CountryAlreadyExistException, DataIntegrityViolationException
     */
    @ExceptionHandler({ BadCredentialsException.class, CountryAlreadyExistException.class,
	    DataIntegrityViolationException.class })
    public ResponseEntity<Object> handleBadRequest(Exception exception, WebRequest request) {

	HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
	String localizedMessage = exception.getLocalizedMessage();
	String message = (StringUtils.isNotEmpty(localizedMessage) ? localizedMessage : httpStatus.getReasonPhrase());

	ErrorResponse<Map<String, String>> body = new ErrorResponse<>(exception, httpStatus, request,
		Map.of("error", message));

	return new ResponseEntity<>(body, httpStatus);
    }

    /**
     * Cette méthode gére l'exception de type MethodArgumentTypeMismatchException
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleTypeMismatch(MethodArgumentTypeMismatchException exception,
	    WebRequest request) {
	String name = exception.getName();
	Class<?> requiredType = exception.getRequiredType();
	String type = requiredType == null ? null : requiredType.getSimpleName();
	Object value = exception.getValue();
	String message = String.format("The provided '%s' should be a valid '%s' and '%s' isn't", name, type, value);
	HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
	ErrorResponse<Map<Object, Object>> body = new ErrorResponse<>(exception, httpStatus, request,
		Map.of("error", message));

	return new ResponseEntity<>(body, httpStatus);
    }

}
