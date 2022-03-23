package com.weilai.socket;

import com.weilai.provider.ServiceProvider;
import com.weilai.service.*;
import com.weilai.transport.RPCServer;
import com.weilai.transport.netty.server.NettyServer;

/**
 * @ClassName SimpleServer
 * @Description: TODO
 */
public class SimpleServer {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        BlogService blogService = new BlogServiceImpl();

        ServiceProvider serviceProvider = new ServiceProvider();

        serviceProvider.providerServiceInterface(userService);
        serviceProvider.providerServiceInterface(blogService);

        RPCServer rpcServer = new NettyServer(serviceProvider);
        rpcServer.start(8899);
    }
}
