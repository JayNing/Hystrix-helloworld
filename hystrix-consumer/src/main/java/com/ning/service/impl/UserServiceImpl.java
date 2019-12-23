package com.ning.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.ning.entity.User;
import com.ning.service.UserService;
import org.springframework.stereotype.Service;

/**
 * author JayNing
 * created by 2019/12/23 11:08
 **/
@Service("userService")
public class UserServiceImpl implements UserService {

    @Override
    public User findUser(String name, String password)  {
        User user = new User(name,15,password,"Purvar");
        return user;
    }

}
