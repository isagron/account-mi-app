package com.isagron.accountmi_server.config;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoClientFactoryBean;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

@Configuration
public class MongoConfig {

    @Value("${db.host}")
    private String dbHost;

    @Value("${spring.data.mongodb.uri}")
    private String mongoUrl;

    @Value("${spring.data.mongodb.database}")
    private String database;

    public @Bean MongoClient reactiveMongoClient() {
        if (mongoUrl!=null) {
            return MongoClients.create(mongoUrl);
        } else {
            return MongoClients.create();
        }
    }

    public @Bean ReactiveMongoTemplate reactiveMongoTemplate() {
        return new ReactiveMongoTemplate(reactiveMongoClient(), database);
    }
}
