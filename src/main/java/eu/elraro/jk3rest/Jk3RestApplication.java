package eu.elraro.jk3rest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;

@EnableCaching
@SpringBootApplication
public class Jk3RestApplication {
	
	private static final Log LOG = LogFactory.getLog(Jk3RestApplication.class);
	
	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {

	   return (container -> {
	        ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/404.html");

	        container.addErrorPages(error404Page);
	   });
	}

	public static void main(String[] args) {
		SpringApplication.run(Jk3RestApplication.class, args);
		LOG.info("JK3REST started.");
	}
}
