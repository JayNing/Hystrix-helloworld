package com.ning.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheKey;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import com.ning.entity.OrderEntity;
import com.ning.service.CacheService;
import com.ning.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * author JayNing
 * created by 2019/12/24 10:07
 **/
@RestController
@RequestMapping("cache")
public class CacheController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private CacheService cacheService;

    @GetMapping("cacheList")
    public List<OrderEntity> cacheList(@CacheKey("username")  String username) throws Exception {
        return orderService.findOrderList(username);
    }

    private String getCacheKey(String username) {
        System.out.println("执行缓存方法 getCacheKey : " + username);
        return username;
    }

    @GetMapping("cacheTest")
    public String cacheTest(Long id) {
        System.out.println("测试缓存方法 cacheTest ");
        String s = cacheService.cacheData(id);
        System.out.println("第一次调用 cacheService.cacheData 结果：" + s);
        String ss = cacheService.cacheData(id);
        System.out.println("第二次调用 cacheService.cacheData 结果：" + ss);


        /**
         * 有了开启缓存自然有清除缓存的方法，用以确保我们在同一请求中进行写操作后，让后续的读操作获取最新的结果，而不是过时的结果。
         */
        //TODO 假设在此执行了写操作
        //清除缓存
        cacheService.flushCacheByAnnotation1(id);
        String sss = cacheService.cacheData(id);
        System.out.println("第三次调用 cacheService.cacheData 结果：" + sss);

        return cacheService.cacheData(id);
    }
}
