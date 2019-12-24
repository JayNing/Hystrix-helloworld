package com.ning.service.impl;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheRemove;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import com.ning.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * author JayNing
 * created by 2019/12/24 12:00
 **/
@Service("cacheService")
public class CacheServiceImpl implements CacheService {

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(commandKey = "commandKey1", fallbackMethod = "fallback")
    @CacheResult(cacheKeyMethod = "getCacheKey")
    @Override
    public String cacheData(Long id) {
        //此次结果会被缓存
//        return getRequestResult();
        return String.valueOf(getRequestResult2());
    }

    private Integer getRequestResult2() {
        return restTemplate.getForObject("http://127.0.0.1:8081/hystrix/cache",Integer.class);
    }

    private String getRequestResult() {
        Object execute = restTemplate.execute("http://127.0.0.1:8081/hystrix/cache",
                HttpMethod.GET, new RequestCallback() {
                    @Override
                    public void doWithRequest(ClientHttpRequest clientHttpRequest) throws IOException {
                        System.out.println("请求结果回调。。。");
                    }
                }, new ResponseExtractor<Object>() {
                    @Override
                    public Object extractData(ClientHttpResponse clientHttpResponse) throws IOException {
                        System.out.println("clientHttpResponse : ");
                        System.out.println(clientHttpResponse);
                        return clientHttpResponse;
                    }
                }, new HashMap<>());
        ClientHttpResponse clientHttpResponse = (ClientHttpResponse) execute;
        try {
            HttpStatus statusCode = clientHttpResponse.getStatusCode();
            if (statusCode.isError()){
                return null;
            }
            InputStream body = clientHttpResponse.getBody();
            byte[] buff = new byte[2014];
            int len = -1;
            String text = null;
            while ((len = body.read(buff)) != -1){
                text = new String(buff,0,len);
                System.out.println("text = " + text);
            }
            if (text != null &&  text.length() > 0){
                return text;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String fallback(Long id){
        return "fallback cache test : " + id;
    }

    /**
     * 使用注解清除缓存 方式1
     * @CacheRemove 必须指定commandKey才能进行清除指定缓存
     */
    @CacheRemove(commandKey = "commandKey1", cacheKeyMethod = "getCacheKey")
    @HystrixCommand
    public void flushCacheByAnnotation1(Long id){
        System.out.println("请求缓存已清空！");
        //这个@CacheRemove注解直接用在更新方法上效果更好
    }

    /**
     * 第一种方法没有使用@CacheKey注解，而是使用这个方法进行生成cacheKey的替换办法
     * 这里有两点要特别注意：
     * 1、这个方法的入参的类型必须与缓存方法的入参类型相同，如果不同被调用会报这个方法找不到的异常
     * 2、这个方法的返回值一定是String类型
     */
    public String getCacheKey(Long id){
        return String.valueOf(id);
    }
}
