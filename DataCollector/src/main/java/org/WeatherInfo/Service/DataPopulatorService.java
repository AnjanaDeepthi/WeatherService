package org.WeatherInfo.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.WeatherInfo.Dao.DataCollectorDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class DataPopulatorService {
	@Value("${yahoo.weatherservice.api}")
	private String yahooPublicApi;
	
	@Value("${thread.pool.size}")
	private int threadPoolSize;

	@Autowired
	private DataCollectorDao dataCollectorDao;

	@Autowired
	private ObjectMapper mapper;

	public void populateWeatherData() {
		/* Get the list of zipcodes from DB. */
		List<String> zipCodes = dataCollectorDao.getZipCodes();
		
		if (zipCodes != null && zipCodes.size() > 0) {
			//For populating the db concurrently
			ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(threadPoolSize);
			
			DataPopulatorThread.yahooPublicApi = yahooPublicApi;

			for (String zipCode : zipCodes) {
				DataPopulatorThread populatorThread = new DataPopulatorThread(zipCode, dataCollectorDao, mapper);
				threadPoolExecutor.execute(populatorThread);
			}

			threadPoolExecutor.shutdown();
		} else {
			System.out.println("There are no zipcodes in the database");
		}
	}
}
