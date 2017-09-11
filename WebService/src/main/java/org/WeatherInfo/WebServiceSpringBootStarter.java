package org.WeatherInfo;

import org.WeatherInfo.Service.Interface.DataCollectorService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

@SpringBootApplication
@PropertySource("classpath:api.properties")
public class WebServiceSpringBootStarter extends SpringBootServletInitializer {
	
	@Value("${rmi.service.url}")
	private String rmiServiceUrl;
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(WebServiceSpringBootStarter.class);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(WebServiceSpringBootStarter.class, args);
	}

	@Bean(name = "rmiProxy")
	RmiProxyFactoryBean getRmiProxyBean() {
		RmiProxyFactoryBean rmiProxy = new RmiProxyFactoryBean();
		rmiProxy.setServiceUrl(rmiServiceUrl);
		rmiProxy.setServiceInterface(DataCollectorService.class);
		return rmiProxy;
	}

}
