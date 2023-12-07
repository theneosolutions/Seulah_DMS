package com.seulah.seulahdms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


@SpringBootApplication
@EnableEurekaClient
public class SeulahDmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeulahDmsApplication.class, args);
    }

}
