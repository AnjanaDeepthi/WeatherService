package org.WeatherInfo.API;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.internal.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/* Checks for Authentication using the Basic-Auth Protocol */

@Component
@Provider
public class AuthenticationFilter implements ContainerRequestFilter {

	private static final String AUTH_KEY = "Authorization";
	private static final String AUTH_PREFIX = "Basic ";
	
	@Value("${api.username}")
	private String username; 
	
	@Value("${api.password}")
	private String password; 

	@Override
	public void filter(ContainerRequestContext request) throws IOException {
		List<String> authHeaders = request.getHeaders().get(AUTH_KEY);
		if (authHeaders != null && authHeaders.size() > 0) {
			String authToken = authHeaders.get(0);
			authToken = authToken.replaceFirst(AUTH_PREFIX, "");
			String decodedAuthToken = Base64.decodeAsString(authToken);
			String[] tokens = decodedAuthToken.split(":");
			if (username.equals(tokens[0]) && password.equals(tokens[1])) // Successfully Authenticated
				return;
		}

		Response authFail = Response.status(Response.Status.UNAUTHORIZED).entity("Authentication Failed").build();
		request.abortWith(authFail);
	}

}
