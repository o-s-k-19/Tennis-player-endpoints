package com.tennis.player.service;

import org.springframework.security.oauth2.jwt.Jwt;

import com.tennis.player.form.LoginRequest;
import com.tennis.player.form.RegisterRequest;
import com.tennis.player.model.AppUser;

import jakarta.validation.Valid;

public interface AuthService {

    AppUser registerNewUser(@Valid RegisterRequest registerForm);

    Jwt login(LoginRequest loginForm);

}
