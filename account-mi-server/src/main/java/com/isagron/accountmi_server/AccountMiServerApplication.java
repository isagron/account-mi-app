package com.isagron.accountmi_server;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
public class AccountMiServerApplication  {

    public static void main(String[] args) {
        SpringApplication.run(AccountMiServerApplication.class, args);
    }


}
