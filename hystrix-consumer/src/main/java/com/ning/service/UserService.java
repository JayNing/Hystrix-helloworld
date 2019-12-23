package com.ning.service;

import com.ning.entity.User;

/**
 * author JayNing
 * created by 2019/12/23 11:07
 **/
public interface UserService {

    public User findUser(String name, String password);

}
