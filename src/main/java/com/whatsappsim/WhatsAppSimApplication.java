package com.whatsappsim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.whatsappsim.config.MongoDbConfig;

@Import(MongoDbConfig.class)
@SpringBootApplication
public class WhatsAppSimApplication {

	public static void main(String[] args) {
		SpringApplication.run(WhatsAppSimApplication.class, args);
	}
}
