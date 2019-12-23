package com.ning.entity;

import lombok.*;

/**
 * author JayNing
 * created by 2019/12/23 11:02
 **/
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String name;
    private int age;
    private String password;
    private String company;
}
