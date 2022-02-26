package com.weilai.client;

import com.weilai.common.Blog;
import com.weilai.common.User;
import com.weilai.service.BlogService;
import com.weilai.service.UserService;

/**
 * @ClassName SocketClient
 * @Description: 测试用消费者（客户端）
 */
public class SocketClient {
    public static void main(String[] args) {
        ClientProxy clientProxy = new ClientProxy("127.0.0.1", 8899);
        UserService proxy = clientProxy.getProxy(UserService.class);

        // method 1
        User userByUserId = proxy.getUserByUserId(10);
        System.out.println("从服务端得到的user为: " + userByUserId);

        // method 2
        User user = User.builder().userName("ABC").id(111).sex(true).build();
        Integer integer = proxy.insertUserId(user);
        System.out.println("向服务端插入数据:" + integer);

        // method 3
        BlogService blogService = clientProxy.getProxy(BlogService.class);
        Blog blogById = blogService.getBlogById(10000);
        System.out.println("从服务端得到的blog为: " + blogById);
    }
}
