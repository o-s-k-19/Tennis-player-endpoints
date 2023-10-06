package com.tennis.player.security;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.server.resource.BearerTokenErrorCodes;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.tennis.player.model.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomBearerTokenAccessDeniedHandler implements AccessDeniedHandler {

    private String realmName;

    /**
     * This is a customisation of the BearerTokenAuthenticationEntryPoint
     * implementation, it contains some custom responses
     * 
     * @throws IOException
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
	    AccessDeniedException accessDeniedException) throws IOException {
	String errorMessage = "Higher privileges required";
	Map<String, String> parameters = new LinkedHashMap<>();
	if (this.realmName != null) {
	    parameters.put("realm", this.realmName);
	}
	if (request.getUserPrincipal() instanceof AbstractOAuth2TokenAuthenticationToken) {
	    parameters.put("error", BearerTokenErrorCodes.INSUFFICIENT_SCOPE);
	    parameters.put("error_description",
		    "The request requires higher privileges than provided by the access token.");
	    errorMessage = "The request requires higher privileges than provided by the access token.";
	    parameters.put("error_uri", "https://tools.ietf.org/html/rfc6750#section-3.1");
	}
	String wwwAuthenticate = computeWWWAuthenticateHeaderValue(parameters);
	response.addHeader(HttpHeaders.WWW_AUTHENTICATE, wwwAuthenticate);
	response.setStatus(HttpStatus.FORBIDDEN.value());

	// added custom response in body
	String message = new ErrorResponse<Map<String, String>>(accessDeniedException, HttpStatus.FORBIDDEN, request,
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
