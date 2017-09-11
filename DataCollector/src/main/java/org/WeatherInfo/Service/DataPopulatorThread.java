package org.WeatherInfo.Service;

import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.WeatherInfo.Dao.DataCollectorDao;
import org.WeatherInfo.Model.WeatherData;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DataPopulatorThread implements Runnable {

	public static String yahooPublicApi;
	
	private final int STATUS_OK = 200;

	private final String RESPONSE_FORMAT = "json";
	
	private ObjectMapper mapper;

	private String zipCode;
	
	private DataCollectorDao dao;
	
	public DataPopulatorThread(String zipCode, DataCollectorDao dao, ObjectMapper mapper){
		this.zipCode = zipCode;
		this.dao = dao;
		this.mapper = mapper;
	}
	
	/* Parse the json response returned from the yahoo weather api and maps to the Weather Data object */
	private WeatherData parseJsonResponse(String responseData) {
		WeatherData data = null;
		try {
			JsonNode node = mapper.readTree(responseData).get("query").get("results").get("channel").get("item")
					.get("forecast");
			data = mapper.treeToValue(node, WeatherData.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	/* Sends a Rest API call to the Yahoo Public Weather API. */
	public Response sendRequestToWeatherApi() {
		String yqlQuery = "select item.forecast from weather.forecast where woeid in (select woeid from geo.places "
				+ "where placetype='Zip' AND text=" + zipCode + ") and u = 'C' limit 1; ";

		Client client = ClientBuilder.newClient();

		WebTarget target = client.target(yahooPublicApi);

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("Sending Request to Yahoo url with zipcode : " + zipCode);

		return target.queryParam("q", yqlQuery).queryParam("format", RESPONSE_FORMAT).request().get();
	}
	
	
	@Override
	public void run() {
		Response response = sendRequestToWeatherApi();
		if (response.getStatus() == STATUS_OK) {
			WeatherData data = parseJsonResponse(response.readEntity(String.class));
			if (data != null) {
				data.setZipCode(zipCode);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				/* Stores the Weather information into the db based on the zipcode */
				dao.storeWeatherData(data);
			}
		} else {
			System.out.println("Request to Weather API for zipCode : " + zipCode + "Failed with status : " + response.getStatus());
		}
	}

}