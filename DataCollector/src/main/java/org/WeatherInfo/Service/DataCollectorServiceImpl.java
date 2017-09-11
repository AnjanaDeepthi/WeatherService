package org.WeatherInfo.Service;

import java.util.ArrayList;
import java.util.List;

import org.WeatherInfo.Dao.DataCollectorDao;
import org.WeatherInfo.Model.WeatherData;
import org.WeatherInfo.Service.Interface.DataCollectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataCollectorServiceImpl implements DataCollectorService {

	@Autowired
	private DataCollectorDao dataCollectorDao;

	@Override
	public WeatherData getWeatherDataForALocation(String zipCode) {
		WeatherData data = null;
		if (zipCode != null) {
			data = dataCollectorDao.getWeatherData(zipCode);
		} else {
			System.out.println("Zipcode is null");
		}
		return data;
	}

	@Override
	public List<WeatherData> getWeatherDataForMultipleLocations(List<String> zipCodes) {
		List<WeatherData> data = new ArrayList<WeatherData>();
		if (zipCodes != null && zipCodes.size() > 0) {
			for (String zipCode : zipCodes) {
				WeatherData weatherData = dataCollectorDao.getWeatherData(zipCode);
				if (weatherData != null)
					data.add(weatherData);
			}
		} else {
			System.out.println("Empty zipcodes list is sent");
		}
		return data;
	}

}
