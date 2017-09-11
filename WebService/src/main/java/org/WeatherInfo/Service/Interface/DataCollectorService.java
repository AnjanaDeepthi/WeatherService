package org.WeatherInfo.Service.Interface;

import java.util.List;

import org.WeatherInfo.Model.WeatherData;

public interface DataCollectorService {

	public WeatherData getWeatherDataForALocation(String zipCode);

	public List<WeatherData> getWeatherDataForMultipleLocations(List<String> zipCodes);
}
