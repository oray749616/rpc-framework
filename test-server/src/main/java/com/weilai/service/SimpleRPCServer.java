package com.weilai.service;

import com.weilai.socket.RPCServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

/**
 * @ClassName SimpleRPCServer
 * @Description: TODO
 */
public class SimpleRPCServer implements RPCServer {
    // 存放服务接口名和service对象的map
    private Map<String, Object> serviceProvide;

    public SimpleRPCServer(Map<String, Object> serviceProvide) {
        this.serviceProvide = serviceProvide;
    }

    @Override
    public void start(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("服务端已启动");

            while (true) {
                Socket socket = serverSocket.accept();

            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("服务端启动失败");
        }
    }

    @Override
    public void stop() {

    }
}
