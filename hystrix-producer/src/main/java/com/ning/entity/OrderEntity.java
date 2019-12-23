package com.ning.entity;

import lombok.*;

import java.util.Date;

/**
 * author JayNing
 * created by 2019/12/23 14:29
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {
    private String orderNo;
    private String username;
    private Date createdTime;
    private Date payTime;
}
