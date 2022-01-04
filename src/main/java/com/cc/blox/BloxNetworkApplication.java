package com.cc.blox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
 

@SpringBootApplication
@EnableScheduling
public class BloxNetworkApplication {

	public static void main(String[] args) {
		SpringApplication.run(BloxNetworkApplication.class, args);
	}

}
