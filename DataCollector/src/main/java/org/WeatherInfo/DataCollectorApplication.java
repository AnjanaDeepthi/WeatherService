package org.WeatherInfo;

import org.WeatherInfo.Service.DataPopulatorService;
import org.WeatherInfo.Service.Interface.DataCollectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.remoting.rmi.RmiServiceExporter;

/*
 * The @SpringBootApplication tells spring that this application is a spring boot application and enables features
 *  such as auto configuration and component scan. 
 */

@SpringBootApplication
@PropertySource("classpath:datacollector.properties")
public class DataCollectorApplication implements CommandLineRunner {

	@Value("${rmi.service.name}")
	private String RMI_SERVICE_NAME;
	
	@Value("${rmi.port}")
	private int RMI_PORT;

	@Autowired
	private DataPopulatorService populatorService;

	@Autowired
	public DataCollectorService service;

	public static void main(String[] args) {
		SpringApplication.run(DataCollectorApplication.class, args);
	}

	/*
	 * This initiates the calls to the yahoo weather api and populating the db with weather info obtained from yahoo!
	 */
	@Override
	public void run(String... arg0) throws Exception {
		populatorService.populateWeatherData();
	}

	/*
	 * Exposes the DataCollectorService interface as an Rmi object
	 */
	@Bean
	RmiServiceExporter exporter() {
		RmiServiceExporter rmiExporter = new RmiServiceExporter();
		rmiExporter.setServiceInterface(DataCollectorService.class);
		rmiExporter.setService(service);
		rmiExporter.setServiceName(RMI_SERVICE_NAME);
		rmiExporter.setRegistryPort(RMI_PORT);
		return rmiExporter;
	}

}
