package com.ning.controller;

import com.alibaba.fastjson.JSONObject;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.ning.entity.User;
import com.ning.service.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;

/**
 * author JayNing
 * created by 2019/12/23 11:02
 **/
@RestController
@RequestMapping("hystrix")
public class HystrixConsumerController {

    @Autowired
    private UserService userService;

    @GetMapping("login")
    public String login(String username, String password, HttpServletRequest request){
        User user = userService.findUser(username, password);
        return JSONObject.toJSONString(user);
    }


    /**
     * 为了请求测试Hystrix请求缓存提供的返回随机数的接口
     */
    @GetMapping("/cache")
    public Integer getRandomInteger(){
        Random random = new Random();
        int randomInt = random.nextInt(99999);
        return randomInt;
    }
}
