package com.doodle.doodledashboard;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableMongock
@SpringBootApplication
public class DoodleDashboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(DoodleDashboardApplication.class, args);
    }

}
