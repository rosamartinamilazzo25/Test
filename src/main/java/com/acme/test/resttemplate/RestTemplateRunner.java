package com.acme.test.resttemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Profile("!test")
public class RestTemplateRunner implements ApplicationRunner  {

	@Autowired
	RestTemplate restTemplate;
	@Value("${application.token.admin}")
	String adminToken;
	@Value("${application.token.user}")
	String userToken;
	
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		HttpHeaders adminHeader = new HttpHeaders();
		adminHeader.set("Authorization", "Bearer " + adminToken);
		HttpHeaders userHeader = new HttpHeaders();
		userHeader.set("Authorization", "Bearer " + userToken);
		
	}

	

}
