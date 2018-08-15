package com.xxx.notes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class DailyTrialApplication {

    public static void main(String[] args) {
        SpringApplication.run(DailyTrialApplication.class, args);
    }
}
