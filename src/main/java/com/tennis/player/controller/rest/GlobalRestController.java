package com.tennis.player.controller.rest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Map;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.view.RedirectView;

import com.tennis.player.config.ApplicationCustomProperties;
import com.tennis.player.model.CustomResponse;
import com.tennis.player.model.ErrorResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class GlobalRestController implements ErrorController {

    private final ApplicationCustomProperties applicationCustomProperties;

    @RequestMapping(path = "/error")
    public ResponseEntity<Object> handler(Exception exception, HttpStatus status, WebRequest webRequest) {
	String message = "Ressource not found";
	HttpStatus httpStatus = status == null ? HttpStatus.NOT_FOUND : status;
	ErrorResponse<Map<String, String>> body = new ErrorResponse<>(exception, httpStatus, webRequest,
		Map.of("error", message));

	return new ResponseEntity<>(body, httpStatus);
    }

    @GetMapping("/")
    public ResponseEntity<Object> home() {

	HttpStatus httpStatus = HttpStatus.OK;
	CustomResponse<String> body = new CustomResponse<>("Available ressources link for the API",
		"Tennis endpoint api", httpStatus);
	body.add(linkTo(methodOn(GlobalRestController.class).home()).withSelfRel(),
		linkTo(PlayerRestController.class).withRel("players"),
		linkTo(CountryRestController.class).withRel("countries"),
		linkTo(methodOn(PlayerRestController.class).getStatistics()).withRel("statistics"),
		linkTo(methodOn(GlobalRestController.class).swaggerUI()).withRel("documentation"));
	return new ResponseEntity<>(body, httpStatus);
    }

    @GetMapping("/api/v1/doc")
    public RedirectView swaggerUI() {
	return new RedirectView(applicationCustomProperties.swaggerUiPath());
    }
}
