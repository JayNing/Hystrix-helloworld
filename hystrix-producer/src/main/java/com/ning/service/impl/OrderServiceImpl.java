package com.ning.service.impl;

import com.ning.entity.OrderEntity;
import com.ning.service.OrderService;
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
import java.time.LocalDateTime;
import java.util.*;

/**
 * author JayNing
 * created by 2019/12/23 14:28
 **/
@Service("orderService")
public class OrderServiceImpl implements OrderService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<OrderEntity> findOrderList(String username) throws Exception {
        List<OrderEntity> list = new ArrayList<>();
        String password = "123456";
        if (validateUsername(username,password)){
            Date date = new Date();
            OrderEntity orderEntity = new OrderEntity("001",username, date,date);
            list.add(orderEntity);
        }else {
            throw new Exception("用户验证未通过");
        }
        return list;
    }

    private boolean validateUsername(String username, String password) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("username",username);
        paramMap.put("password",password);
        Object execute = restTemplate.execute("http://127.0.0.1:8081/hystrix/login?username=" + username + "&password=" + password,
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
                }, paramMap);
        ClientHttpResponse clientHttpResponse = (ClientHttpResponse) execute;
        try {
            HttpStatus statusCode = clientHttpResponse.getStatusCode();
            if (statusCode.isError()){
                return false;
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
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
