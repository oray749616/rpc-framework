package com.weilai.service;

import com.weilai.annotation.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName HelloServiceImpl
 * @Description: TODO
 */

@Slf4j
@Service
public class HelloServiceImpl implements HelloService {

    static {
        System.out.println("helloServiceImpl被创建");
    }

    @Override
    public String hello(String name) {
        return "Hello, " + name;
    }
}
