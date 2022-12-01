package com.example.speedsideproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpeedSideProject {

    public static void main(String[] args) {
        SpringApplication.run(SpeedSideProject.class, args);
    }

}
