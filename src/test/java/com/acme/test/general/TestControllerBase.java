package com.acme.test.general;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import lombok.Getter;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@Getter
public class TestControllerBase extends TestBase{
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Autowired
	@Qualifier("AdminHttpHeaders")
	private HttpHeaders adminHeaders;
	
	@Autowired
	@Qualifier("UserHttpHeaders")
	private HttpHeaders userHeaders;
	
	@Autowired
	@Qualifier("AdminEntity")
	private HttpEntity adminEntity;
	
	@Autowired
	@Qualifier("UserEntity")
	private HttpEntity userEntity;
	
	

}

