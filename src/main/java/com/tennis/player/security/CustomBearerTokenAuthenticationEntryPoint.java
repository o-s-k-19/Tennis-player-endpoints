package com.tennis.player.security;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.server.resource.BearerTokenError;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.StringUtils;

import com.tennis.player.model.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomBearerTokenAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private String realmName;

    /**
     * This is a customisation of the BearerTokenAuthenticationEntryPoint
     * implementation
     * 
     * @throws IOException
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
	    AuthenticationException authException) throws IOException {
	HttpStatus status = HttpStatus.UNAUTHORIZED;
	String errorMessage = "Authentication required";
	Map<String, String> parameters = new LinkedHashMap<>();
	if (this.realmName != null) {
	    parameters.put("realm", this.realmName);
	}
	if (authException instanceof OAuth2AuthenticationException oAuth2AuthenticationException) {
	    OAuth2Error error = oAuth2AuthenticationException.getError();
	    parameters.put("error", error.getErrorCode());
	    if (StringUtils.hasText(error.getDescription())) {
		errorMessage = error.getDescription();
		parameters.put("error_description", error.getDescription());
	    }
	    if (StringUtils.hasText(error.getUri())) {
		parameters.put("error_uri", error.getUri());
	    }
	    if (error instanceof BearerTokenError bearerTokenError) {
		if (StringUtils.hasText(bearerTokenError.getScope())) {
		    parameters.put("scope", bearerTokenError.getScope());
		}
		status = ((BearerTokenError) error).getHttpStatus();
	    }
	}
	String wwwAuthenticate = computeWWWAuthenticateHeaderValue(parameters);
	response.addHeader(HttpHeaders.WWW_AUTHENTICATE, wwwAuthenticate);
	response.setStatus(status.value());

	// added custom response in body
	String message = new ErrorResponse<Map<String, String>>(authException, status, request,
		Map.of("error", errorMessage)).toString();
	response.setContentType(MediaType.APPLICATION_JSON_VALUE);
	response.getWriter().write(message);
    }

    /**
     * Set the default realm name to use in the bearer token error response
     * 
     * @param realmName
     */
    public void setRealmName(String realmName) {
	this.realmName = realmName;
    }

    private static String computeWWWAuthenticateHeaderValue(Map<String, String> parameters) {
	StringBuilder wwwAuthenticate = new StringBuilder();
	wwwAuthenticate.append("Bearer");
	if (!parameters.isEmpty()) {
	    wwwAuthenticate.append(" ");
	    int i = 0;
	    for (Map.Entry<String, String> entry : parameters.entrySet()) {
		wwwAuthenticate.append(entry.getKey()).append("=\"").append(entry.getValue()).append("\"");
		if (i != parameters.size() - 1) {
		    wwwAuthenticate.append(", ");
		}
		i++;
	    }
	}
	return wwwAuthenticate.toString();
    }

}
