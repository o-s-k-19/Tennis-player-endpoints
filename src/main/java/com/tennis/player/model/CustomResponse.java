package com.tennis.player.model;

import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;

import com.tennis.player.utilities.AppUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomResponse<T> extends RepresentationModel<CustomResponse<T>> {

    private String timestamp;
    private int status;
    private String description;
    private T response;

    public CustomResponse(T result, String description, HttpStatus httpStatus) {
	super();
	this.timestamp = AppUtils.formatTimestamp(LocalDateTime.now());
	this.status = httpStatus.value();
	this.description = description;
	this.response = result;
    }

}
