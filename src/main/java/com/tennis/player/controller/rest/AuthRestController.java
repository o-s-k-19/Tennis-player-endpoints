package com.tennis.player.controller.rest;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tennis.player.form.LoginRequest;
import com.tennis.player.form.RegisterRequest;
import com.tennis.player.model.AppUser;
import com.tennis.player.model.CustomResponse;
import com.tennis.player.service.AuthService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthRestController {

    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<CustomResponse<String>> signUp(@Valid @RequestBody RegisterRequest registerForm) {
	AppUser user = authService.registerNewUser(registerForm);
	HttpStatus httpStatus = HttpStatus.CREATED;
	CustomResponse<String> body = new CustomResponse<>(
		"Dear " + user.getUsername() + ", You have been registered !", "User registration", httpStatus);

	return new ResponseEntity<>(body, httpStatus);
    }

    @PostMapping("/login")
    public ResponseEntity<CustomResponse<Map<String, Object>>> signIn(@RequestBody LoginRequest loginForm) {
	HttpStatus httpStatus = HttpStatus.OK;
	Jwt jwt = authService.login(loginForm);
	CustomResponse<Map<String, Object>> body = new CustomResponse<>(
		Map.of("valid until", jwt.getExpiresAt(), "access-token", jwt.getTokenValue()), "Authorization token",
		httpStatus);

	return new ResponseEntity<>(body, httpStatus);
    }

}
