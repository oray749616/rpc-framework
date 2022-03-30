package com.weilai.client;

import com.weilai.common.Blog;
import com.weilai.common.User;
import com.weilai.proxy.ClientProxy;
import com.weilai.service.BlogService;
import com.weilai.service.UserService;
import com.weilai.transport.RPCClient;
import com.weilai.transport.netty.client.NettyClient;

/**
 * @ClassName TestClient
 * @Description: 测试用消费者（客户端）
 */
public class TestClient {
    public static void main(String[] args) {
        // 构建一个使用netty传输的客户端
        RPCClient client = new NettyClient("127.0.0.1", 8899);
        // 把客户端传入代理
        ClientProxy proxy = new ClientProxy(client);
        // 代理客户端根据不同的服务，获得代理类
        UserService userService = proxy.getProxy(UserService.class);
        BlogService blogService = proxy.getProxy(BlogService.class);
        // 调用方法
        User userByUserId = userService.getUserByUserId(10);
        System.out.println("从服务端得到的user为：" + userByUserId);

        User user = User.builder().userName("张三").id(100).sex(true).build();
        Integer integer = userService.insertUserId(user);
        System.out.println("向服务端插入数据：" + integer);

        Blog blogById = blogService.getBlogById(10000);
        System.out.println("从服务端得到的blog为：" + blogById);
    }
}
