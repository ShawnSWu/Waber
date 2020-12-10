package com.shawn.match;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class PricingApplication {

    public static void main(String[] args) {
        SpringApplication.run(PricingApplication.class, args);
    }

    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

    @Bean(name = "tripServiceUrl")
    public String tripServiceUrl(){
        return "http://0.0.0.0:8082";
    }

    @Bean(name = "matchServiceUrl")
    public String matchServiceUrl(){
        return "http://0.0.0.0:8081";
    }

    @Bean(name = "userServiceUrl")
    public String userServiceUrl(){
        return "http://0.0.0.0:8080";
    }
}
