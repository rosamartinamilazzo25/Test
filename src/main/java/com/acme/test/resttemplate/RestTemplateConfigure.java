package com.acme.test.resttemplate;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfigure {

	@Bean
	public RestTemplate getRestTemplate() {
		RestTemplate r = new RestTemplate();
	
		return r;
	}
	

}
