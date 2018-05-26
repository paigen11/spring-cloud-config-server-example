package com.myexample.myexampleconfigserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class MyexampleConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyexampleConfigServerApplication.class, args);
	}
}
