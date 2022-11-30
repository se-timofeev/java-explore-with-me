package ru.practicum.ewm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainAPP {
    public static final String APP_NAME = "ewm-main";

    public static void main(String[] args) {
        SpringApplication.run(MainAPP.class, args);
    }

}
