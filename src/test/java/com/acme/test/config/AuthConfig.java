package com.acme.test.config;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Configuration
public class AuthConfig {
	public static final String USER_ADMIN = "admin";
	public static final String USER_USER = "user";
	
	@Value("${application.jwtSecret}")
	public String jwtSecret;
	
	
	private String createToken(String user) {
		Map<String, Object> claims = new HashMap<String, Object>();
		claims.put("sub", user);
		Date today = new Date();
		
		return Jwts.builder()
				.setSubject(user)
				.setClaims(claims)
				.setIssuedAt(  today  )
				.setExpiration(  new Date( today.getTime() + 999999999    )   )
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
	}
	
	@Bean("AdminToken")
	public String adminToken() {
		
		return createToken(USER_ADMIN);
		
	}
	

	@Bean("UserToken")
	public String userToken() {
		return createToken(USER_USER);
	}
	

	@Bean("AdminHttpHeaders")
	public HttpHeaders adminHeaders() {
		HttpHeaders h = new HttpHeaders();
		
		h.set("Authorization",  "Bearer "  + adminToken());
		return h;
		
	}

	@Bean("UserHttpHeaders")
	public HttpHeaders userHeaders() {
		HttpHeaders h = new HttpHeaders();
		
		h.set("Authorization",  "Bearer "  + userToken());
		return h;
	}
	

	@Bean("AdminEntity")
	public HttpEntity adminEntity() {
		return  new HttpEntity ( adminHeaders()   );
	}


	@Bean("UserEntity")
	public HttpEntity userEntity() {
		return  new HttpEntity ( userHeaders()   );
	}
}
