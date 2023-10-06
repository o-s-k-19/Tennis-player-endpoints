package com.tennis.player.service;

import java.time.Instant;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tennis.player.form.LoginRequest;
import com.tennis.player.form.RegisterRequest;
import com.tennis.player.model.AppUser;
import com.tennis.player.repository.AppUserRepository;
import com.tennis.player.security.JwtProvider;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private AppUserRepository appUserRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtProvider jwtProvider;

    @Override
    public AppUser registerNewUser(@Valid RegisterRequest registerForm) {
	AppUser user = new AppUser();
	user.setUsername(registerForm.getUsername());
	user.setEmail(registerForm.getEmail());
	user.setPassword(passwordEncoder.encode(registerForm.getPassword()));
	user.setCreated(Instant.now());
	user.setEnabled(true);
	return appUserRepository.save(user);
    }

    @Override
    public Jwt login(LoginRequest loginForm) {
	Authentication authenticate = authenticationManager.authenticate(
		new UsernamePasswordAuthenticationToken(loginForm.getUsername(), loginForm.getPassword()));
	SecurityContextHolder.getContext().setAuthentication(authenticate);

	return jwtProvider.generateJwtToken(authenticate);
    }

}
