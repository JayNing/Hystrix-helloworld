package com.ning.service;

import com.ning.entity.OrderEntity;

import java.util.List;

/**
 * author JayNing
 * created by 2019/12/23 14:28
 **/
public interface OrderService {
    List<OrderEntity> findOrderList(String username) throws Exception;
}
