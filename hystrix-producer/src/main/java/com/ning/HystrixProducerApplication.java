package com.ning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * author JayNing
 * created by 2019/12/23 10:45
 **/
@SpringBootApplication
@ServletComponentScan(basePackages = {"com.ning.filter"}) //这一句完成了配置，Springboot的”懒理念“真的厉害。
public class HystrixProducerApplication {
    public static void main(String[] args) {
        SpringApplication.run(HystrixProducerApplication.class, args);
    }
}
