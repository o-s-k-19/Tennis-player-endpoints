package com.tennis.player.security;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtEncoder jwtEncoder;
    
    public Jwt generateJwtToken(User user) {

	String authorities = user.getAuthorities().stream().map(GrantedAuthority::getAuthority)
		.collect(Collectors.joining(" "));

	JwtClaimsSet claims = JwtClaimsSet.builder().issuer("Tennis-api-endpoint").issuedAt(Instant.now())
		.expiresAt(Instant.now().plusMillis(Duration.ofDays(1).toMillis())).subject(user.getUsername())
		.claim("scope", authorities).build();
	return this.jwtEncoder.encode(JwtEncoderParameters.from(claims));

    }

    public Jwt generateJwtToken(Authentication authentication) {
	User user = (User) authentication.getPrincipal();
	return generateJwtToken(user);
    }
}
