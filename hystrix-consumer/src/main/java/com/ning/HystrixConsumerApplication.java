package com.ning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * author JayNing
 * created by 2019/12/23 10:45
 **/
@SpringBootApplication
public class HystrixConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(HystrixConsumerApplication.class, args);
    }
}
