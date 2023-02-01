package com.example.speedsideproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.annotation.PostConstruct;
import java.util.Locale;
import java.util.TimeZone;

@EnableJpaAuditing
@SpringBootApplication
@EnableEurekaClient
public class SpeedSideProject {

    @PostConstruct
    public void started() {
        // timezone UTC 셋팅
        // TimeZone.setDefault(TimeZone.getTimeZone("UTC")); //utc설정
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul")); //korea time 설정
        Locale.setDefault(Locale.KOREA); //locale 설정
    }

    public static void main(String[] args) {
        SpringApplication.run(SpeedSideProject.class, args);
    }

}
