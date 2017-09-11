package org.WeatherInfo;

import javax.ws.rs.ApplicationPath;

import org.WeatherInfo.API.AuthenticationFilter;
import org.WeatherInfo.API.WeatherInfoAPI;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
@ApplicationPath("/v1")
public class JerseyConfig extends ResourceConfig {

	public JerseyConfig() {
		register(AuthenticationFilter.class);
		register(WeatherInfoAPI.class);
	}

}
