package com.example.securitybestpractice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class SecurityBestPracticeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityBestPracticeApplication.class, args);
    }

}
