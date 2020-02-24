package com.armzilla.ha;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringbootEntry {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootEntry.class, args);
    }

}
