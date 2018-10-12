package com.xxx.notes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(exclude = {JpaRepositoriesAutoConfiguration.class,
        org.activiti.spring.boot.JpaProcessEngineAutoConfiguration.class,
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
        org.activiti.spring.boot.SecurityAutoConfiguration.class})
@EnableTransactionManagement
public class DailyTrialApplication {

    public static void main(String[] args) {
        SpringApplication.run(DailyTrialApplication.class, args);
    }
}
