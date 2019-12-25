package com.ning.controller;

import com.ning.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * author JayNing
 * created by 2019/12/24 22:17
 **/
@RestController
@RequestMapping("jvm")
public class JVMController {

    private List<User> list = new ArrayList<>();

    /**
     * 测试垃圾回收
     */
    @GetMapping("heap")
    public void heap(){
        while (true){
            try {
                list.add(new User());
                Thread.sleep(1L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
