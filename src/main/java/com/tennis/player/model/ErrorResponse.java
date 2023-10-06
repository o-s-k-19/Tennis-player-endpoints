package com.tennis.player.model;

import java.time.LocalDateTime;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;

import com.tennis.player.utilities.AppUtils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Cette classe genere une reponse plus detaillé de l'erreur
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse<T> {

    private String timestamp;
    private int status;
    private String message;
    private T errors;
    private String path;
    //private String type;

    public ErrorResponse(Exception exception, HttpStatus httpStatus, WebRequest webRequest, T errors) {
	this.timestamp = AppUtils.formatTimestamp(LocalDateTime.now());
	this.status = httpStatus.value();
	this.message = httpStatus.getReasonPhrase();
	this.errors = errors;
	this.path = webRequest.getDescription(false);
	//this.type = exception.getClass().getSimpleName();
    }

    public ErrorResponse(Exception exception, HttpStatus httpStatus, HttpServletRequest request, T errors) {
	this.timestamp = AppUtils.formatTimestamp(LocalDateTime.now());
	this.status = httpStatus.value();
	this.message = httpStatus.getReasonPhrase();
	this.errors = errors;
	this.path = request.getPathInfo();
	//this.type = exception.getClass().getSimpleName();
    }

    @Override
    public String toString() {
	return new JSONObject(this).toString();
    }
}
