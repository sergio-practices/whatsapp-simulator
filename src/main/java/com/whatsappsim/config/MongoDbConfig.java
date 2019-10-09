package com.whatsappsim.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;

@Configuration
@EnableMongoRepositories(basePackages = {"com.whatsappsim.mongodb.repository"})
public class MongoDbConfig extends AbstractMongoConfiguration{

    @Bean
    MongoTransactionManager transactionManager(MongoDbFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }
	
    @Bean
    public MongoClient mongoClient() {
    	MongoClientOptions mongoClientOptions = MongoClientOptions.builder()
                .connectTimeout(3000)
                .build();
    	ServerAddress serverAdress = new ServerAddress("localhost", 27017);
        return new MongoClient(serverAdress, mongoClientOptions);
    }

	@Override
	protected String getDatabaseName() {
		return "whatsappsim";
	}
	
}
