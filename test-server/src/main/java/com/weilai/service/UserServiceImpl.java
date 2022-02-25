package com.weilai.service;

import com.weilai.common.User;

import java.util.Random;
import java.util.UUID;

/**
 * @ClassName UserServiceImpl
 * @Description: TODO
 */
public class UserServiceImpl implements UserService{
    @Override
    public User getUserByUserId(Integer id) {
        System.out.println("客户端查询了id为 " + id + " 的用户");

        // 模拟从数据库中查询用户的行为
        Random random = new Random();
        User user = User.builder().userName(UUID.randomUUID().toString())
                .id(id)
                .sex(random.nextBoolean()).build();

        return user;
    }
}
