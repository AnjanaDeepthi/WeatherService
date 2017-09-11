package org.WeatherInfo.Service.Interface;

import java.util.List;

import org.WeatherInfo.Model.WeatherData;

/* 
 * This interface is exposed as an Rmi service to the Rmi client 
 */
public interface DataCollectorService {

	/*
	 * Returns the weather information corresponding to a location
	 */
	public WeatherData getWeatherDataForALocation(String zipCode);
	
	/*
	 * Gets the weather information for multiple locations using their zipcodes
	 */
	public List<WeatherData> getWeatherDataForMultipleLocations(List<String> zipCodes);

}
