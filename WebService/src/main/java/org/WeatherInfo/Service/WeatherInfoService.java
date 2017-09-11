package org.WeatherInfo.Service;

import java.util.List;

import org.WeatherInfo.Model.WeatherData;
import org.WeatherInfo.Service.Interface.DataCollectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WeatherInfoService {

	/* Spring's Rmi logic injects the proxy object into the dataservice */
	@Autowired
	private DataCollectorService dataService;

	/* The below methods uses the datacollector's service methods to get the weather info */
	public WeatherData getWeatherForALocation(String zipCode) {
		return dataService.getWeatherDataForALocation(zipCode);
	}

	public List<WeatherData> getWeatherForLocationList(List<String> zipCodes) {
		return dataService.getWeatherDataForMultipleLocations(zipCodes);
	}

}
