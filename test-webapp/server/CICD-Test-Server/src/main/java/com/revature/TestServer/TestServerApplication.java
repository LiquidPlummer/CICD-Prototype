package com.revature.TestServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages =
		"com.revature.TestServer.Controllers, " +
		"com.revature.TestServer.Repositories" +
		"com.revature.TestServer.Services")
public class TestServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestServerApplication.class, args);
	}

}
