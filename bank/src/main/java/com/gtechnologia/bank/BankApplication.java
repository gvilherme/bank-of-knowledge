package com.gtechnologia.bank;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BankApplication {

    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(BankApplication.class, args);
    }
}
