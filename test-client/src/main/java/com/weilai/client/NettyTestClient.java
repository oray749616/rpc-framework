package com.weilai.client;

import com.weilai.proxy.ClientProxy;
import com.weilai.service.HelloService;
import com.weilai.transport.RPCClient;
import com.weilai.transport.netty.client.NettyClient;

/**
 * @ClassName TestClient
 * @Description: 测试用消费者（客户端）
 */
public class NettyTestClient {
    public static void main(String[] args) {
        // 构建一个使用netty传输的客户端
        RPCClient client = new NettyClient("127.0.0.1", 8899);
        // 把客户端传入代理
        ClientProxy proxy = new ClientProxy(client);
        // 代理客户端根据不同的服务，获得代理类
        HelloService helloService = proxy.getProxy(HelloService.class);
        // 调用方法
        String result = helloService.hello("world");
        System.out.println(result);
    }
}
