package com.tennis.player.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.tennis.player.security.CustomBearerTokenAccessDeniedHandler;
import com.tennis.player.security.CustomBearerTokenAuthenticationEntryPoint;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final RsaKeyProperties rsaKeys;
    private final ApplicationCustomProperties applicationCustomProperties;
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
	    throws Exception {
	return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
	return httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
		.oauth2ResourceServer(oauth2RessourceServer -> oauth2RessourceServer.jwt(Customizer.withDefaults()))
		.exceptionHandling(exceptions -> exceptions
			.authenticationEntryPoint(new CustomBearerTokenAuthenticationEntryPoint())
			.accessDeniedHandler(new CustomBearerTokenAccessDeniedHandler()))
		.csrf(csrf -> csrf.disable()).cors(cors -> cors.disable())
		.authorizeHttpRequests(authorize -> authorize
			.requestMatchers(
				AntPathRequestMatcher.antMatcher("/"),
				AntPathRequestMatcher.antMatcher(applicationCustomProperties.basePath()+"/auth/register"),
				AntPathRequestMatcher.antMatcher(applicationCustomProperties.basePath()+"/auth/login"),
				AntPathRequestMatcher.antMatcher(HttpMethod.GET, applicationCustomProperties.basePath()+"/**"),
				AntPathRequestMatcher.antMatcher("/h2-console/**"),
				AntPathRequestMatcher.antMatcher("/v3/api-docs/**"),
				AntPathRequestMatcher.antMatcher("/swagger-ui/**"),
				AntPathRequestMatcher.antMatcher("/webjars/**"))
			.permitAll()
			.requestMatchers(
				AntPathRequestMatcher.antMatcher(HttpMethod.POST, applicationCustomProperties.basePath()+"/**"),
				AntPathRequestMatcher.antMatcher(HttpMethod.PUT, applicationCustomProperties.basePath()+"/**"),
				AntPathRequestMatcher.antMatcher(HttpMethod.DELETE, applicationCustomProperties.basePath()+"/**"))
			.authenticated().anyRequest().authenticated())
		.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
	return new BCryptPasswordEncoder();
    }

    @Bean
    JwtDecoder jwtDecoder() {
	return NimbusJwtDecoder.withPublicKey(this.rsaKeys.publicKey()).build();
    }

    @Bean
    JwtEncoder jwtEncoder() {
	JWK jwk = new RSAKey.Builder(this.rsaKeys.publicKey()).privateKey(this.rsaKeys.privateKey()).build();
	JWKSource<SecurityContext> jwksource = new ImmutableJWKSet<>(new JWKSet(jwk));
	return new NimbusJwtEncoder(jwksource);
    }

}
