package com.jcoder.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

	@Bean
	@LoadBalanced // we need to tell the rest template as well, we are connected to service registry,
	// you need to load balance your request,
	// when multiple services registered to service registry, it will load balance request for us
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}

}
