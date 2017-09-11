package org.WeatherInfo.API;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.WeatherInfo.Model.WeatherData;
import org.WeatherInfo.Service.WeatherInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Path("/forecast")
public class WeatherInfoAPI {

	@Autowired
	private WeatherInfoService service;

	/*  
	 * Returns the weather info for a location based on its zipcode
	 * Usage : http://host:port/weatherinfo/v1/forecast/zipcode
	 * Example : http://localhost:8083/v1/forecast/73301
	 * Method Type : GET
	 * Authentication : Required
	 * Response : {
    		"high": 30,
    		"low": 16,
    		"zipCode": "73301",
    		"Weather Condition": "Sunny"
		}
	 */
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{zipCode}")
	public Response getWeatherInfo(@PathParam("zipCode") String zipCode) {
		if (zipCode == null) {
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}

		WeatherData data = service.getWeatherForALocation(zipCode);

		if (data == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}

		return Response.ok().entity(data).build();
	}

	/*  
	 * Returns the weather info for multiple locations based on their zipcodes
	 * Usage : http://host:port/weatherinfo/v1/forecast/zipcode=value&zipcode=value
	 * Method Type : GET
	 * Authentication : Required
	 * Example Request : http://localhost:8083/weatherinfo/v1/forecast?zipCode=73301&zipCode=75124
	 * Example Response : {
    	[
    		{
        		"high": 30,
        		"low": 16,
        		"zipCode": "73301",
        		"Weather Condition": "Sunny"
    		},
    		{
        		"high": 28,
        		"low": 16,
        		"zipCode": "75124",
        		"Weather Condition": "Sunny"
    		}
		]
	 }
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWeatherForMultipleLocations(@QueryParam("zipcode") List<String> zipCodes) {
		if (zipCodes == null || zipCodes.isEmpty()) {
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}

		List<WeatherData> data = service.getWeatherForLocationList(zipCodes);

		if (data == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}

		GenericEntity entity = new GenericEntity<List<WeatherData>>(data) {
		};

		return Response.ok().entity(entity).build();
	}

}
