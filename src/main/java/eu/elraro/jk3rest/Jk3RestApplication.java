package eu.elraro.jk3rest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class Jk3RestApplication {
	
	private static final Log LOG = LogFactory.getLog(Jk3RestApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(Jk3RestApplication.class, args);
		LOG.info("JK3REST started.");
	}
}
